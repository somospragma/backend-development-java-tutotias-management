package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.domain.model.User;
import java.util.Optional;

public interface FindUserByIdUseCase {
    Optional<User> findUserById(String id);
}