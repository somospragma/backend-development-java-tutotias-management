package com.pragma.shared.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OkResponseDto<T> {
    private String message;
    private T data;
    private LocalDateTime timestamp;

   
    public static <T> OkResponseDto<T> of(String message, T data) {
        return new OkResponseDto<>(message, data, LocalDateTime.now());
    }

    public static <T> OkResponseDto<T> of(String message ) {
        return new OkResponseDto<>(message, null,LocalDateTime.now());
    }
}