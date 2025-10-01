package anhlv.auth.interceptor;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor để inject OpenTelemetry trace context vào MDC
 * Chạy TRƯỚC khi controller method được thực thi
 */
@Component
public class OpenTelemetryInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenTelemetryInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Inject trace context vào MDC trước khi controller chạy
        injectTraceContextToMDC();
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cleanup không cần thiết vì OpenTelemetry agent sẽ tự quản lý
    }
    
    private void injectTraceContextToMDC() {
        try {
            Span currentSpan = Span.current();
            SpanContext spanContext = currentSpan.getSpanContext();
            
            if (spanContext.isValid()) {
                String traceId = spanContext.getTraceId();
                String spanId = spanContext.getSpanId();
                
                // Inject trace context vào ThreadContext (Log4j2 MDC)
                ThreadContext.put("traceId", traceId);
                ThreadContext.put("spanId", spanId);
                ThreadContext.put("serviceName", "auth-service");
            }
        } catch (Exception e) {
            logger.error("Error injecting trace context to MDC", e);
        }
    }
}
