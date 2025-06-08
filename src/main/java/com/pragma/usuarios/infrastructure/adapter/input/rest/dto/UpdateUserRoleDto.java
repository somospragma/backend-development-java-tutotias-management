package com.pragma.usuarios.infrastructure.adapter.input.rest.dto;

import com.pragma.usuarios.domain.model.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleDto {
    @NotBlank(message = "El ID del usuario es obligatorio")
    private String id;
    
    @NotNull(message = "El rol es obligatorio")
    private RolUsuario role;
}