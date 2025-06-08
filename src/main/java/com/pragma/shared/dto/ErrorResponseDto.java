package com.pragma.shared.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public static  ErrorResponseDto of(String message,  Map<String, String> errors) {
        return new ErrorResponseDto(message, errors, LocalDateTime.now());
    }

    public static  ErrorResponseDto of(String message ) {
        return new ErrorResponseDto(message, null, LocalDateTime.now());
    }
}