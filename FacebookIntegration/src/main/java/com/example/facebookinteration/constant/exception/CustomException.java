package com.example.facebookinteration.constant.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public CustomException(int statusCode, String message) {
        super(message);
        this.errorResponse = ErrorResponse.builder()
                .statusCode(statusCode)
                .message(message)
                .timestamp(LocalDateTime.now()) // Thêm thời gian vào đối tượng ErrorResponse
                .build();
    }

}
