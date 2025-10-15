package anhlv.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBufferFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;


/**
 * GlobalFilter hợp nhất: Ghi log Request/Response và thêm TraceID vào Response Body.
 */
@Component
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LogManager.getLogger(RequestResponseLoggingFilter.class);
    private static final int MAX_LOG_BODY_LENGTH = 2048;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final long startTime = System.currentTimeMillis();
        logRequest(exchange);

        ServerHttpResponse originalResponse = exchange.getResponse();
        
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // Use a string builder to accumulate body parts
                StringBuilder bodyBuilder = new StringBuilder();
                
                return super.writeWith(Flux.from(body)
                    .doOnNext(dataBuffer -> {
                        // Append each chunk to the builder
                        try {
                            bodyBuilder.append(StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer()));
                        } catch (Exception e) {
                            logger.warn("Could not decode response body chunk for logging", e);
                        }
                    })
                    // This runs when the body publisher completes
                    .doFinally(signalType -> {
                        // Store the captured body (even if empty) for the final log
                        exchange.getAttributes().put("responseBody", bodyBuilder.toString());
                    })
                );
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
            .doFinally(signalType -> {
                String responseBody = exchange.getAttribute("responseBody");
                // If the body is null (e.g., never captured) or empty, log appropriately
                if (responseBody == null || responseBody.isEmpty()) {
                    responseBody = "<empty body>";
                }
                logResponse(startTime, exchange, getStatusCodeValue(exchange.getResponse()), responseBody);
            });
    }
    
    private int getStatusCodeValue(ServerHttpResponse response) {
        if (response.getStatusCode() != null) {
            return response.getStatusCode().value();
        }
        return 0; // Or some default value
    }

    private String sanitizeBody(String body, int maxLength) {
        if (body == null || body.isEmpty()) {
            return "<empty>";
        }
        String sanitized = body.replaceAll("\"password\"\\s*:\\s*\".*?\"", "\"password\":\"***\"")
                               .replaceAll("\"token\"\\s*:\\s*\".*?\"", "\"token\":\"***\"");
        if (sanitized.length() > maxLength) {
            return sanitized.substring(0, maxLength) + "...<truncated>";
        }
        return sanitized;
    }

    private void logRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Đọc cached body từ attribute (sau khi cacheRequestBody filter đã chạy)
        Object cachedBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        String bodyStr = "";
        if (cachedBody != null) {
            bodyStr = cachedBody.toString();
        }
        
        logger.info("=========================REQUEST START=================================");
        logger.info("PATH: {}, METHOD: {}, BODY: {}",
                request.getPath().value(),
                request.getMethod(),
                sanitizeBody(bodyStr, MAX_LOG_BODY_LENGTH));
    }
    
    private void logResponse(long startTime, ServerWebExchange exchange, int statusCode, String body) {
        long duration = System.currentTimeMillis() - startTime;
        String traceId = exchange.getAttribute("TRACE_ID");
        String spanId = exchange.getAttribute("SPAN_ID");
        ThreadContext.put("serviceName", "gateway-service");
        ThreadContext.put("traceId", traceId);
        ThreadContext.put("spanId", spanId);

        logger.info("STATUS: {}, DURATION: {}ms, BODY: {}",
                statusCode,
                duration,
                sanitizeBody(body, MAX_LOG_BODY_LENGTH));
        logger.info("=========================REQUEST END===================================");
        ThreadContext.clearMap();
    }
    
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // Chạy SAU các route filters (bao gồm cacheRequestBody)
    }
}
