package com.pragma.sistematutorias.shared.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
    
    public ErrorResponseDto(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponseDto(String message, Map<String, String> errors) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }
}