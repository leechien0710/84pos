package com.company.common.apiresponse;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic API Response wrapper class
 * 
 * @param <T> Type of data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private int code;
    private String message;
    private T data;
    private String traceId;
    private String spanId;
    
    // Private constructor để force sử dụng factory methods
    private ApiResponse() {}
    
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    // Getters
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
    
    public String getTraceId() {
        return traceId;
    }
    
    public String getSpanId() {
        return spanId;
    }
    
    // Setters (for Jackson serialization)
    public void setCode(int code) {
        this.code = code;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    
    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }
    
    // Factory methods
    /**
     * Tạo success response với data
     * 
     * @param <T> Type of data
     * @param data Data payload
     * @return ApiResponse with code 200 and success message
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }
    
    /**
     * Tạo success response với custom message
     * 
     * @param <T> Type of data
     * @param data Data payload
     * @param message Custom success message
     * @return ApiResponse with code 200 and custom message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }
    
    /**
     * Tạo error response
     * 
     * @param <T> Type of data
     * @param code Error code
     * @param message Error message
     * @return ApiResponse with error code and message
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    /**
     * Tạo error response với data
     * 
     * @param <T> Type of data
     * @param code Error code
     * @param message Error message
     * @param data Error data payload
     * @return ApiResponse with error code, message and data
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
    
    /**
     * Kiểm tra response có thành công không
     * 
     * @return true nếu code = 200
     */
    public boolean isSuccess() {
        return code == 200;
    }
    
    /**
     * Kiểm tra response có lỗi không
     * 
     * @return true nếu code != 200
     */
    public boolean isError() {
        return code != 200;
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
