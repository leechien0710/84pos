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
        
        // Không log gì cả - chỉ track thời gian
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Log response body nếu có
        try {
            if (response instanceof org.springframework.web.util.ContentCachingResponseWrapper) {
                org.springframework.web.util.ContentCachingResponseWrapper responseWrapper = 
                    (org.springframework.web.util.ContentCachingResponseWrapper) response;
                byte[] content = responseWrapper.getContentAsByteArray();
                if (content.length > 0) {
                    String responseBody = new String(content, response.getCharacterEncoding());
                    logger.info("RESPONSE BODY: {}", responseBody);
                }
            }
        } catch (Exception e) {
            logger.debug("Could not log response body: {}", e.getMessage());
        }
        
        // Tính thời gian xử lý request
        Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("============= REQUEST END ============= ({}ms)", duration);
        } else {
            logger.info("============= REQUEST END =============");
        }
    }
    
    private boolean isImportantHeader(String lowerHeaderName) {
        return lowerHeaderName.equals("content-type") ||
               lowerHeaderName.equals("authorization") ||
               lowerHeaderName.equals("user-agent") ||
               lowerHeaderName.equals("x-forwarded-for") ||
               lowerHeaderName.equals("x-real-ip");
    }
    
    
}
