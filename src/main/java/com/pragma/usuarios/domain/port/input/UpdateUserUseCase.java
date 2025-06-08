package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.domain.model.User;
import java.util.Optional;

public interface UpdateUserUseCase {
    Optional<User> updateUser(String id, User user);
}