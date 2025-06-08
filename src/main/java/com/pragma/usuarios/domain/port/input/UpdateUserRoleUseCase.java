package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;

import java.util.Optional;

public interface UpdateUserRoleUseCase {
    Optional<User> updateUserRole(String id, RolUsuario role);
}