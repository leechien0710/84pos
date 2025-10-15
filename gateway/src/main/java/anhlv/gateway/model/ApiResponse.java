package anhlv.gateway.model;

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
    private boolean error;
    private boolean success;
    
    // Private constructor để force sử dụng factory methods
    private ApiResponse() {}
    
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.error = code != 200;
        this.success = code == 200;
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
    
    public boolean isError() {
        return error;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    // Setters
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
    
    public void setError(boolean error) {
        this.error = error;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    // Factory methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }
    
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}

