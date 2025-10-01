package com.example.facebookinteration.dto;

import lombok.Data;

@Data
public class SendTextMessageRequest {
    private String userId;
    private String message;
} 