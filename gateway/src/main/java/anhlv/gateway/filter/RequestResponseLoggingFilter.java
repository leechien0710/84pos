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
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put("startTime", startTime); // Lưu thời gian bắt đầu
        ServerHttpRequest request = exchange.getRequest();
        
        // Log Request từ body đã được cache
        String requestBody = exchange.getAttributeOrDefault(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR, "");
        logRequest(request, requestBody);

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    final int statusCode = getStatusCode().value();

                    return super.writeWith(fluxBody.flatMap(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        DataBufferUtils.release(dataBuffer);

                        String originalBody = new String(content, StandardCharsets.UTF_8);
                        byte[] modifiedBytes = content; // Mặc định là body gốc

                        String contentType = getHeaders().getFirst("Content-Type");
                        if (contentType != null && contentType.contains("application/json")) {
                            try {
                                String traceId = exchange.getAttribute("TRACE_ID");
                                JsonNode jsonNode = objectMapper.readTree(originalBody);
                                if (jsonNode.isObject()) {
                                    ObjectNode objectNode = (ObjectNode) jsonNode;
                                    objectNode.put("traceId", traceId);
                                    String modifiedBody = objectMapper.writeValueAsString(objectNode);
                                    modifiedBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
                                }
                            } catch (Exception e) {
                                logger.error("Error modifying response body to add traceId", e);
                            }
                        }
                        
                        // Log response SAU KHI đã sửa đổi
                        logResponse(startTime, exchange, statusCode, new String(modifiedBytes, StandardCharsets.UTF_8));
                        
                        getHeaders().setContentLength(modifiedBytes.length);
                        return Mono.just(exchange.getResponse().bufferFactory().wrap(modifiedBytes));
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().request(request).response(decoratedResponse).build());
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

    private void logRequest(ServerHttpRequest request, String body) {
        logger.info("=========================REQUEST START=================================");
        logger.info("PATH: {}, BODY: {}",
                request.getPath().value(),
                sanitizeBody(body, MAX_LOG_BODY_LENGTH));
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
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
