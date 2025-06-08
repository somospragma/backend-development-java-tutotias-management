package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.domain.model.User;

public interface CreateUserUseCase {
    User createUser(User user);
}