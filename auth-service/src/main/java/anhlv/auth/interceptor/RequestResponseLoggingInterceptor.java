package anhlv.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.util.Enumeration;

/**
 * Interceptor để log chi tiết request và response
 */
// @Component // ĐÃ BỎ đăng ký Interceptor này để tránh trùng lặp log; giữ file cho tham khảo
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);
    private static final String REQUEST_START_TIME = "REQUEST_START_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Ghi nhận thời gian bắt đầu request
        request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Xây dựng log message sau khi body đã được đọc bởi Spring (@RequestBody)
        StringBuilder logMessage = new StringBuilder();

        // PATH
        logMessage.append("PATH: ").append(request.getMethod()).append(" ").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            logMessage.append("?").append(request.getQueryString());
        }

        // REQ HEADER (giới hạn các header quan trọng)
        logMessage.append(" | REQ HEADER: ");
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String lower = headerName.toLowerCase();
            if (!isAllowedHeader(lower)) continue;
            String headerValue = request.getHeader(headerName);
            if ("authorization".equals(lower) && headerValue != null) {
                headerValue = maskToken(headerValue);
            }
            if (headers.length() > 0) headers.append(", ");
            headers.append(headerName).append("=").append(headerValue);
        }
        logMessage.append(headers.length() > 0 ? headers.toString() : "(no headers)");

        // REQ BODY (đọc từ ContentCachingRequestWrapper hoặc attribute)
        logMessage.append(" | REQ BODY: ");
        String requestBody = getRequestBodyFromInputStream(request);
        if (requestBody != null && !requestBody.isEmpty()) {
            String safeBody = sanitizeBody(requestBody);
            if (safeBody.length() > 2048) {
                safeBody = safeBody.substring(0, 2048) + "...<truncated>";
            }
            logMessage.append(safeBody);
        } else {
            logMessage.append("(empty)");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("=========================================");
            logger.debug("{}", logMessage.toString());
            logger.debug("=========================================");
        }
    }
    
    private String getRequestBodyFromInputStream(HttpServletRequest request) {
        try {
            // Sử dụng ContentCachingRequestWrapper để đọc request body mà không consume input stream
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                byte[] content = wrapper.getContentAsByteArray();
                if (content.length > 0) {
                    return new String(content, wrapper.getCharacterEncoding());
                }
            }
            
            // Fallback: thử đọc từ attribute nếu có
            Object requestBody = request.getAttribute("REQUEST_BODY");
            if (requestBody != null) {
                return requestBody.toString();
            }
            
            return null;
        } catch (Exception e) {
            logger.debug("Error reading request body: {}", e.getMessage());
            return null;
        }
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
