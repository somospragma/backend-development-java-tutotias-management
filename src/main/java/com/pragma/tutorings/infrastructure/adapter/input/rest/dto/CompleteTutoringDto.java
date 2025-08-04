package com.pragma.tutorings.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTutoringDto {
    @NotBlank(message = "El ID del usuario es requerido")
    private String userId;
    
    @NotBlank(message = "El acta final es requerida para completar la tutoría")
    private String finalActUrl;
}