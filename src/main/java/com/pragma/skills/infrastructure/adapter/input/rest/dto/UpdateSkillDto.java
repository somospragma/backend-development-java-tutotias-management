package com.pragma.skills.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSkillDto {
    @NotBlank(message = "El ID de la habilidad es obligatorio")
    private String id;
    
    @NotBlank(message = "El nombre de la habilidad es obligatorio")
    private String name;
}