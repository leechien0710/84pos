package com.example.businessservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class CustomException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3222412019034721436L;
    private final ErrorResponse errorResponse;

    public CustomException(HttpStatus status, String message) {
        super(message); // Gọi constructor của RuntimeException để giữ message
        this.errorResponse = ErrorResponse.builder()
                .statusCode(status.value()) // Lấy mã HTTP từ HttpStatus
                .message(message)
                .build();
    }
}
