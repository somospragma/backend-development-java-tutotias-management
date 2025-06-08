package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.domain.model.User;

import java.util.Optional;

public interface UpdateTutoringLimitUseCase {
    Optional<User> updateTutoringLimit(String id, int activeTutoringLimit);
}