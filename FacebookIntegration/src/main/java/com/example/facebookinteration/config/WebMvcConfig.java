package com.example.facebookinteration.config;

import com.example.facebookinteration.interceptor.OpenTelemetryInterceptor;
import com.example.facebookinteration.interceptor.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration để đăng ký interceptors
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private OpenTelemetryInterceptor openTelemetryInterceptor;
    
    @Autowired
    private RequestResponseLoggingInterceptor requestResponseLoggingInterceptor;
    
    // Đăng ký interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký OpenTelemetry MDC interceptor với priority cao nhất
        registry.addInterceptor(openTelemetryInterceptor)
                .addPathPatterns("/**")  // Apply to all paths
                .excludePathPatterns("/api/face/ws/**")  // Exclude WebSocket endpoints
                .order(1);  // Highest priority
                
        // Đăng ký Request/Response logging interceptor
        registry.addInterceptor(requestResponseLoggingInterceptor)
                .addPathPatterns("/**")  // Apply to all paths
                .excludePathPatterns("/api/face/ws/**")  // Exclude WebSocket endpoints (CRITICAL: prevents input stream consumption)
                .order(2);  // Second priority
    }
}
