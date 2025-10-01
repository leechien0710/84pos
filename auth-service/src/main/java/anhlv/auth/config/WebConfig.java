package anhlv.auth.config;

import anhlv.auth.interceptor.OpenTelemetryInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private OpenTelemetryInterceptor openTelemetryInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký OpenTelemetry MDC interceptor với priority cao nhất
        registry.addInterceptor(openTelemetryInterceptor)
                .addPathPatterns("/**")  // Apply to all paths
                .order(1);  // Highest priority
    }
}
