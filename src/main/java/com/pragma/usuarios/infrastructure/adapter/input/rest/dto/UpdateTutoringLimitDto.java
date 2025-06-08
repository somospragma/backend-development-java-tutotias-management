package com.pragma.usuarios.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutoringLimitDto {
    @NotBlank(message = "El ID del usuario es obligatorio")
    private String id;
    
    @NotNull(message = "El límite de tutorías es obligatorio")
    @Min(value = 0, message = "El límite de tutorías debe ser mayor o igual a 0")
    private Integer activeTutoringLimit;

}