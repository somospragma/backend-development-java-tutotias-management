package com.pragma.tutorings.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutoringDto {
    @NotBlank(message = "El ID de la solicitud de tutoría es obligatorio")
    private String tutoringRequestId;
    
    @NotBlank(message = "El ID del tutor es obligatorio")
    private String tutorId;
    
    @NotBlank(message = "Los objetivos de la tutoría son obligatorios")
    private String objectives;
}