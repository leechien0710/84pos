package com.example.facebookinteration.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Advice để log response body trước khi gửi về client
 * Order = 2 để chạy sau ApiResponseAdvice (order = 1)
 */
@ControllerAdvice
@Order(2)
public class ResponseLoggingAdvice implements ResponseBodyAdvice<Object> {
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseLoggingAdvice.class);
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        
        // Không log gì cả - response body sẽ được log ở interceptor
        return body;
    }
}
