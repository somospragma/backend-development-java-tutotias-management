package com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutoringRequestDto {
    @NotNull(message = "El ID del estudiante es obligatorio")
    private String tuteeId;
    
    // Lista de habilidades opcional
    private List<String> skillIds;
    
    @NotBlank(message = "La descripción de la necesidad es obligatoria")
    private String needsDescription;
}