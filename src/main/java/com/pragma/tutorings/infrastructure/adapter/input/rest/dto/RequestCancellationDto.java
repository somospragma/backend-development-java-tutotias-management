package com.pragma.tutorings.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCancellationDto {
    
    @NotBlank(message = "La razón de cancelación es requerida")
    @Size(max = 500, message = "La razón de cancelación no puede exceder 500 caracteres")
    private String reason;
    
    private String userId; // Se establece automáticamente desde el contexto
}