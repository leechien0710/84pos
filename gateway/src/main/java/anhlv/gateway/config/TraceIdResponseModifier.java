package anhlv.gateway.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Global Filter để thêm traceId vào response JSON từ downstream services
 * Giống như face service thêm traceId cho client để trace
 */
@Component
public class TraceIdResponseModifier implements GlobalFilter, Ordered {
    
    private static final Logger logger = LogManager.getLogger(TraceIdResponseModifier.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        
        // Chỉ modify response cho JSON content type
        ServerHttpResponse originalResponse = exchange.getResponse();
        
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            
            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                
                // Kiểm tra content type có phải JSON không
                String contentType = getHeaders().getFirst("Content-Type");
                if (contentType == null || !contentType.contains("application/json")) {
                    return super.writeWith(body);
                }

                if (body instanceof Flux) {
                    DataBufferFactory bufferFactory = originalResponse.bufferFactory();
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    // Inject trace context
                    String traceId = (String) exchange.getAttributes().get("TRACE_ID");
                    String spanId = (String) exchange.getAttributes().get("SPAN_ID");
                    String serviceName = (String) exchange.getAttributes().get("SERVICE_NAME");

                    if (traceId != null) {
                        ThreadContext.put("traceId", traceId);
                        ThreadContext.put("spanId", spanId);
                        ThreadContext.put("serviceName", serviceName != null ? serviceName : "gateway-service");
                    }

                    // Modify response body để thêm traceId
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        try {
                            // Đọc response JSON
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            String responseBody = new String(content, StandardCharsets.UTF_8);

                            // Parse JSON và thêm traceId
                            JsonNode jsonNode = objectMapper.readTree(responseBody);
                            if (jsonNode.isObject()) {
                                ObjectNode objectNode = (ObjectNode) jsonNode;

                                // Thêm traceId vào response JSON (giống face service)
                                objectNode.put("traceId", traceId);
                                objectNode.put("spanId", spanId);
                                objectNode.put("gateway", "84pos-gateway");

                                // Convert back to JSON string
                                String modifiedResponse = objectMapper.writeValueAsString(objectNode);

                                logger.info("Added traceId to response: {}", traceId);
                                logger.info("Modified response: {}", modifiedResponse.length() > 500 ?
                                        modifiedResponse.substring(0, 500) + "..." : modifiedResponse);

                                // Tạo DataBuffer mới với modified content
                                byte[] modifiedBytes = modifiedResponse.getBytes(StandardCharsets.UTF_8);

                                // Update Content-Length header
                                getHeaders().setContentLength(modifiedBytes.length);

                                return bufferFactory.wrap(modifiedBytes);
                            } else {
                                logger.debug("Response is not JSON object, skipping traceId injection");
                                return bufferFactory.wrap(content);
                            }

                        } catch (Exception e) {
                            logger.error("Error modifying response body: {}", e.getMessage());
                            // Return original content nếu có lỗi
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            return bufferFactory.wrap(content);
                        }
                    }));
                }
                return super.writeWith(body);
            }
        };
        
        // Tạo exchange mới với decorated response
        ServerWebExchange decoratedExchange = exchange.mutate()
                .response(decoratedResponse)
                .build();
        
        return chain.filter(decoratedExchange);
    }
    
    @Override
    public int getOrder() {
        // Chạy sau OpenTelemetryFilter nhưng trước ResponseLoggingFilter
        return 5;
    }
}
