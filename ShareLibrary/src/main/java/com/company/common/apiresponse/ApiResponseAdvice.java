package com.company.common.apiresponse;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Global response body advice để tự động wrap response thành ApiResponse format
 * 
 * Nếu response body đã là ApiResponse thì giữ nguyên
 * Nếu chưa thì wrap bằng ApiResponse.success(body)
 * Order = 1 để chạy trước ResponseLoggingAdvice
 */
@RestControllerAdvice
@Order(1)
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Áp dụng cho tất cả method trả về Object (không phải void)
        // Bao gồm cả ResponseEntity để wrap response body
        return !returnType.getParameterType().equals(void.class);
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        
        // Nếu body đã là ApiResponse thì giữ nguyên
        if (body instanceof ApiResponse) {
            return body;
        }
        
        // Nếu body là ResponseEntity, lấy data từ body của ResponseEntity
        if (body instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) body;
            Object responseBody = responseEntity.getBody();
            
            // Nếu responseEntity body đã là ApiResponse thì giữ nguyên
            if (responseBody instanceof ApiResponse) {
                return responseBody;
            }
            
            // Wrap responseEntity body bằng ApiResponse
            return ApiResponse.success(responseBody);
        }
        
        // Nếu body là null thì trả về success response với data null
        if (body == null) {
            return ApiResponse.success(null);
        }
        
        // Force sử dụng JSON converter
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        // Wrap body bằng ApiResponse.success()
        return ApiResponse.success(body);
    }
}
