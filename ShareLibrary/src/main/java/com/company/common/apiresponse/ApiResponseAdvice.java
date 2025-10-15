package com.company.common.apiresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
@Order(1)
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    // Constructor để config ObjectMapper với JSR310 support
    public ApiResponseAdvice() {
        // ObjectMapper sẽ được inject bởi Spring, nhưng đảm bảo có JSR310 support
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(void.class);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        // Debug: Log ObjectMapper configuration
        logger.info("ObjectMapper modules: {}", objectMapper.getRegisteredModuleIds());
        logger.info("ObjectMapper features: WRITE_DATES_AS_TIMESTAMPS={}", 
                   objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

        if (body instanceof ApiResponse) {
            return body;
        }

        if (body instanceof ResponseEntity) {
            // Logic xử lý ResponseEntity của bạn đã ổn
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) body;
            Object responseBody = responseEntity.getBody();
            if (responseBody instanceof ApiResponse) {
                return responseBody;
            }
            ApiResponse<?> apiResponse = ApiResponse.success(responseBody);
            return ResponseEntity.status(responseEntity.getStatusCode())
                                 .headers(responseEntity.getHeaders())
                                 .body(apiResponse);
        }

        // Xử lý đặc biệt cho String để tránh ClassCastException
        if (body instanceof String) {
            // Cải thiện: Đặt Content-Type là JSON tại đây
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                ApiResponse<String> apiResponse = ApiResponse.success((String) body);
                return objectMapper.writeValueAsString(apiResponse);
            } catch (Exception e) {
                // Log lỗi để debug và monitoring
                logger.error("Error writing JSON response for String type: {}", body, e);
                throw new RuntimeException("Error writing JSON response", e);
            }
        }

        if (body == null) {
            return ApiResponse.success(null);
        }

        // Với các object khác, wrap chúng lại.
        // Không cần set Content-Type ở đây, MessageConverter sẽ tự làm.
        return ApiResponse.success(body);
    }
}