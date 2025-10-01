package anhlv.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Filter để wrap request/response cho phép đọc body nhiều lần
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final int MAX_LOG_BODY_LENGTH = 2048; // 2KB

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        long startTimeMs = System.currentTimeMillis();
        
        // Wrap request và response để có thể cache content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
        
        try {
            // START/HEADER logging đã được chuyển sang AOP để đảm bảo trace có sẵn từ Interceptor

            // Tiếp tục chain với wrapped objects
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                // Build request metadata (không log lại body để tránh trùng với AOP)
                String method = httpRequest.getMethod();
                String path = httpRequest.getRequestURI();

                // Build response log
                int status = responseWrapper.getStatus();
                byte[] bodyBytes = responseWrapper.getContentAsByteArray();
                Charset charset = getCharset(responseWrapper.getCharacterEncoding());
                String responseBody = bodyBytes.length > 0 ? new String(bodyBytes, charset) : "";
                String safeResponseBody = truncate(responseBody);
                long durationMs = System.currentTimeMillis() - startTimeMs;

                // In phần còn lại của block theo thứ tự cố định (không in REQ BODY lần 2)
                logger.info("RESP STATUS: {}, DURATION_MS: {}, BODY: {}", status, durationMs, safeResponseBody);
                logger.info("=========================================");
            } catch (Exception e) {
                logger.debug("Could not log request/response: {}", e.getMessage());
            }
            // Đảm bảo response body được copy về client
            responseWrapper.copyBodyToResponse();
        }
    }

    private Charset getCharset(String encoding) {
        try {
            if (encoding != null && !encoding.isEmpty()) {
                return Charset.forName(encoding);
            }
        } catch (Exception ignored) {
        }
        return StandardCharsets.UTF_8;
    }

    private String truncate(String text) {
        if (text == null) return null;
        if (text.length() <= MAX_LOG_BODY_LENGTH) return text;
        return text.substring(0, MAX_LOG_BODY_LENGTH) + "...<truncated>";
    }

    private String getCachedRequestBody(ContentCachingRequestWrapper wrapper) {
        try {
            byte[] content = wrapper.getContentAsByteArray();
            if (content.length == 0) return null;
            String encoding = wrapper.getCharacterEncoding();
            Charset charset = getCharset(encoding);
            return new String(content, charset);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildAllowedHeadersString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String lower = name.toLowerCase();
            if (!isAllowedHeader(lower)) continue;
            String value = request.getHeader(name);
            if ("authorization".equals(lower) && value != null) {
                value = maskToken(value);
            }
            if (headers.length() > 0) headers.append(", ");
            headers.append(name).append("=").append(value);
        }
        return headers.length() > 0 ? headers.toString() : "(no headers)";
    }

    private boolean isAllowedHeader(String lowerKey) {
        return lowerKey.equals("content-type")
                || lowerKey.equals("content-length")
                || lowerKey.equals("authorization")
                || lowerKey.equals("x-trace-id")
                || lowerKey.equals("x-span-id")
                || lowerKey.equals("x-b3-traceid")
                || lowerKey.equals("x-b3-spanid")
                || lowerKey.equals("traceparent")
                || lowerKey.equals("tracestate")
                || lowerKey.equals("user-agent");
    }

    private String sanitizeBody(String body) {
        if (body == null || body.isEmpty()) return body;
        String result = body;
        result = result.replaceAll("\"password\"\\s*:\\s*\".*?\"", "\"password\":\"***\"");
        result = result.replaceAll("\"token\"\\s*:\\s*\".*?\"", "\"token\":\"***\"");
        result = result.replaceAll("\"authorization\"\\s*:\\s*\".*?\"", "\"authorization\":\"***\"");
        return result;
    }

    private String maskToken(String headerValue) {
        if (headerValue == null) return null;
        if (headerValue.toLowerCase().startsWith("bearer ")) {
            return "Bearer ****";
        }
        return "****";
    }
}