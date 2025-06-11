package com.pragma.feedbacks.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackDto {
    @NotBlank(message = "El ID del evaluador es obligatorio")
    private String evaluatorId;
    
    @NotBlank(message = "El ID de la tutoría es obligatorio")
    private String tutoringId;
    
    @NotBlank(message = "La puntuación es obligatoria")
    private String score;
    
    @NotBlank(message = "Los comentarios son obligatorios")
    private String comments;
}