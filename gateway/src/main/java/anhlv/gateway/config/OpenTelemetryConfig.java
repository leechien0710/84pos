package anhlv.gateway.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.GlobalOpenTelemetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenTelemetry Configuration cho Gateway
 * Đảm bảo OpenTelemetry instance được inject đúng cách
 */
@Configuration
public class OpenTelemetryConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenTelemetryConfig.class);
    
    @Bean
    public OpenTelemetry openTelemetry() {
        logger.info("Creating OpenTelemetry bean from GlobalOpenTelemetry");
        
        // Lấy OpenTelemetry instance từ Global (được set bởi agent)
        OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
        
        logger.info("OpenTelemetry instance created: {}", openTelemetry.getClass().getName());
        
        return openTelemetry;
    }
}