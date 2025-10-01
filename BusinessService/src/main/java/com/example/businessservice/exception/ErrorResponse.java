package com.example.businessservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7765868325584199986L;
    private int statusCode;
    private String message;
}
