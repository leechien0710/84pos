package anhlv.gateway.exception;

import com.company.common.apiresponse.CustomException;
import com.company.common.apiresponse.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.ThreadContext;

/**
 * Exception handler cho Gateway để xử lý tất cả exceptions
 * Sử dụng ErrorWebExceptionHandler cho WebFlux
 */
@Component
@Order(-1) // Ưu tiên cao nhất để xử lý trước ShareLibrary
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GatewayExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private String getCurrentTraceId(ServerWebExchange exchange) {
        try {
            // Lấy từ request headers
            String traceParent = exchange.getRequest().getHeaders().getFirst("traceparent");
            if (traceParent != null && !traceParent.isEmpty()) {
                // traceparent format: 00-{traceId}-{spanId}-01
                String[] parts = traceParent.split("-");
                if (parts.length >= 2) {
                    return parts[1];
                }
            }
            
            // Fallback: tạo trace ID mới theo OpenTelemetry format (32 hex chars)
            java.util.UUID uuid = java.util.UUID.randomUUID();
            return uuid.toString().replace("-", "");
            
        } catch (Exception e) {
            logger.error("Error getting trace ID", e);
        }
        return "no-trace";
    }
    
    private String getCurrentSpanId(ServerWebExchange exchange) {
        try {
            // Lấy từ request headers
            String traceParent = exchange.getRequest().getHeaders().getFirst("traceparent");
            if (traceParent != null && !traceParent.isEmpty()) {
                // traceparent format: 00-{traceId}-{spanId}-01
                String[] parts = traceParent.split("-");
                if (parts.length >= 3) {
                    return parts[2];
                }
            }
            
            // Fallback: tạo span ID mới theo OpenTelemetry format (16 hex chars)
            java.util.UUID uuid = java.util.UUID.randomUUID();
            return uuid.toString().replace("-", "").substring(0, 16);
            
        } catch (Exception e) {
            logger.error("Error getting span ID", e);
        }
        return "no-span";
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        long startTime = exchange.getAttributeOrDefault("startTime", System.currentTimeMillis());
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        
        // Set trace ID và span ID vào ThreadContext để hiển thị trong log pattern
        String traceId = getCurrentTraceId(exchange);
        String spanId = getCurrentSpanId(exchange);
        ThreadContext.put("traceId", traceId);
        ThreadContext.put("spanId", spanId);
        ThreadContext.put("serviceName", "gateway-service");

        // Xử lý CustomException (từ authentication filter, business logic)
        if (ex instanceof CustomException) {
            CustomException customEx = (CustomException) ex;
            logger.warn("Gateway error: {} - {}", customEx.getCode(), customEx.getMessage());
            
            response.setStatusCode(HttpStatus.valueOf(customEx.getCode()));
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<Object> errorResponse = ApiResponse.error(customEx.getCode(), customEx.getMessage());
            errorResponse.setTraceId(traceId);
            
            try {
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                logRequestEnd(startTime, customEx.getCode(), jsonResponse);
                DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                logRequestEnd(startTime, customEx.getCode(), "{\"error\":\"Failed to serialize error response\"}");
                logger.error("Error serializing error response", e);
                return Mono.error(e);
            }
        }
        
        // Xử lý NotFoundException
        if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<Object> errorResponse = ApiResponse.error(404, "API endpoint not found. Please check the path.");
            errorResponse.setTraceId(traceId);
            
            try {
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                logRequestEnd(startTime, 404, jsonResponse);
                DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                logRequestEnd(startTime, 404, "{\"error\":\"Failed to serialize error response\"}");
                logger.error("Error serializing error response", e);
                return Mono.error(e);
            }
        }
        
        // Xử lý ResponseStatusException với 404
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException statusEx = (ResponseStatusException) ex;
            if (statusEx.getStatusCode() == HttpStatus.NOT_FOUND) {
                response.setStatusCode(HttpStatus.NOT_FOUND);
                response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                
                ApiResponse<Object> errorResponse = ApiResponse.error(404, "API endpoint not found. Please check the path.");
                errorResponse.setTraceId(traceId);
                
                try {
                    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                    logRequestEnd(startTime, 404, jsonResponse);
                    DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
                    return response.writeWith(Mono.just(buffer));
                } catch (JsonProcessingException e) {
                    logRequestEnd(startTime, 404, "{\"error\":\"Failed to serialize error response\"}");
                    logger.error("Error serializing error response", e);
                    return Mono.error(e);
                }
            }
        }
        
        // Xử lý Connection refused (service không chạy)
        if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
            logger.warn("Service unavailable: {}", ex.getMessage());
            
            response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<Object> errorResponse = ApiResponse.error(503, "Service temporarily unavailable. Please try again later.");
            errorResponse.setTraceId(traceId);
            
            try {
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                logRequestEnd(startTime, 503, jsonResponse);
                DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                logRequestEnd(startTime, 503, "{\"error\":\"Failed to serialize error response\"}");
                logger.error("Error serializing error response", e);
                return Mono.error(e);
            }
        }
        
        // Xử lý các exception khác (RuntimeException, Exception, etc.)
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        ApiResponse<Object> errorResponse = ApiResponse.error(500, "Internal server error. Please try again later.");
        errorResponse.setTraceId(traceId);
        
        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            logRequestEnd(startTime, 500, jsonResponse);
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            logRequestEnd(startTime, 500, "{\"error\":\"Failed to serialize error response\"}");
            logger.error("Error serializing error response", e);
            return Mono.error(e);
        }
    }

    private void logRequestEnd(long startTime, int statusCode, String body) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("STATUS: {}, DURATION: {}ms, BODY: {}",
                statusCode,
                duration,
                body.length() > 2048 ? body.substring(0, 2048) + "..." : body);
        logger.info("=========================REQUEST END===================================");
        ThreadContext.clearMap();
    }
}