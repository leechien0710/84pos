package com.example.facebookinteration.interceptor;

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
@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);
    private static final String REQUEST_START_TIME = "REQUEST_START_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Ghi nhận thời gian bắt đầu request
        request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
        
        // Xây dựng log message trên 1 dòng
        StringBuilder logMessage = new StringBuilder();
        
        // PATH
        logMessage.append("PATH: ").append(request.getMethod()).append(" ").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            logMessage.append("?").append(request.getQueryString());
        }
        
        // REQ BODY
        logMessage.append(" | REQ BODY: ");
        String requestBody = getRequestBodyFromInputStream(request);
        if (requestBody != null && !requestBody.isEmpty()) {
            request.setAttribute("REQUEST_BODY", requestBody);
            logMessage.append(requestBody);
        } else {
            logMessage.append("(empty)");
        }
        
        // Log tất cả trên 1 dòng
        logger.info("=========================================");
        logger.info("{}", logMessage.toString());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("=========================================");
    }
    
    private String getRequestBodyFromInputStream(HttpServletRequest request) {
        try {
            // Đọc request body từ input stream
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        } catch (Exception e) {
            logger.debug("Error reading request body from input stream: {}", e.getMessage());
            return null;
        }
    }
    
}
