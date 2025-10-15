package anhlv.gateway.exception;

/**
 * Custom exception class cho business logic errors
 */
public class CustomException extends RuntimeException {
    
    private final int code;
    private final String message;
    
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}

