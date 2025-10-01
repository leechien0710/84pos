package com.company.common.apiresponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler để xử lý tất cả exceptions và trả về ApiResponse format
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Xử lý MethodArgumentNotValidException (Validation errors)
     * 
     * @param ex MethodArgumentNotValidException
     * @return ApiResponse với code 400 và validation error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation error: {}", ex.getMessage());
        
        // Lấy field error đầu tiên
        String errorMessage = "Validation failed";
        if (ex.getBindingResult().hasFieldErrors()) {
            errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }
        
        ApiResponse<Object> response = ApiResponse.error(400, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Xử lý ConstraintViolationException (Bean validation errors)
     * 
     * @param ex ConstraintViolationException
     * @return ApiResponse với code 400 và validation error message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(400, "Validation failed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Xử lý IllegalArgumentException (Invalid argument errors)
     * 
     * @param ex IllegalArgumentException
     * @return ApiResponse với code 400 và error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Xử lý CustomException (Business logic errors)
     * 
     * @param ex CustomException
     * @return ApiResponse với custom code và message
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        // Không log tại đây để tránh trùng lặp; log nên được thực hiện tại nơi phát sinh lỗi
        
        ApiResponse<Object> response = ApiResponse.error(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getCode())).body(response);
    }
    
    
    /**
     * Xử lý RuntimeException (General runtime errors)
     * 
     * @param ex RuntimeException
     * @return ApiResponse với code 500 và error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime error: {}", ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error(500, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Xử lý NoHandlerFoundException (404 - API endpoint not found)
     * 
     * @param ex NoHandlerFoundException
     * @return ApiResponse với code 404 và error message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(404, "API endpoint not found: " + ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Xử lý HttpRequestMethodNotSupportedException (405 - Method not allowed)
     * 
     * @param ex HttpRequestMethodNotSupportedException
     * @param request HttpServletRequest để lấy URL
     * @return ApiResponse với code 405 và error message
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = String.format("HTTP method '%s' not supported for this endpoint. Supported methods: %s", 
                                     ex.getMethod(), String.join(", ", ex.getSupportedMethods()));
        
        ApiResponse<Object> response = ApiResponse.error(405, message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
    
    /**
     * Xử lý Exception (Catch-all for other exceptions)
     * 
     * @param ex Exception
     * @return ApiResponse với code 500 và generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        // Bỏ qua favicon.ico request
        if (ex.getMessage() != null && ex.getMessage().contains("favicon.ico")) {
            logger.debug("Favicon.ico request ignored: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
        
        // Bỏ qua static resource requests
        if (ex.getMessage() != null && ex.getMessage().contains("No static resource")) {
            logger.debug("Static resource request ignored: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
        
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error(500, "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
