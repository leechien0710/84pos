package anhlv.gateway.filter;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * GlobalFilter để inject OpenTelemetry trace context vào Log4j2 MDC.
 * Chạy đầu tiên để đảm bảo mọi log sau đó đều có trace context.
 */
@Component
public class OpenTelemetryFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LogManager.getLogger(OpenTelemetryFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Lấy current span từ context của OpenTelemetry agent
        Span currentSpan = Span.current();
        SpanContext spanContext = currentSpan.getSpanContext();

        final String traceId;
        final String spanId;

        if (spanContext.isValid()) {
            traceId = spanContext.getTraceId();
            spanId = spanContext.getSpanId();
            logger.debug("Using agent span: traceId={}, spanId={}", traceId, spanId);
        } else {
            logger.warn("OpenTelemetry agent did not create a valid span. Tracing context will be empty.");
            traceId = "";
            spanId = "";
        }

        // 1. Inject vào ThreadContext (MDC) cho logs
        ThreadContext.put("serviceName", "gateway-service");
        ThreadContext.put("traceId", traceId);
        ThreadContext.put("spanId", spanId);

        // 2. Lưu vào exchange attributes để các filter khác sử dụng
        exchange.getAttributes().put("TRACE_ID", traceId);
        exchange.getAttributes().put("SPAN_ID", spanId);
        
        // 3. Truyền context xuống reactive pipeline
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put("traceId", traceId).put("spanId", spanId))
                .doFinally(signalType -> ThreadContext.clearMap()); // Dọn dẹp MDC khi request kết thúc
    }

    @Override
    public int getOrder() {
        // Chạy đầu tiên trong tất cả các filter
        return Ordered.HIGHEST_PRECEDENCE;
    }
}