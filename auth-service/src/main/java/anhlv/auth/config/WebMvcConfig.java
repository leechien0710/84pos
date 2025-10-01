package anhlv.auth.config;

import anhlv.auth.interceptor.OpenTelemetryInterceptor;
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
    
    // Bỏ Interceptor log request/response cuối luồng để tránh trùng lặp với Filter/AOP

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký OpenTelemetry MDC interceptor với priority cao nhất
        registry.addInterceptor(openTelemetryInterceptor)
                .addPathPatterns("/**")  // Apply to all paths
                .order(1);  // Highest priority
    }
}
