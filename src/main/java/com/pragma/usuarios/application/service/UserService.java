package com.pragma.usuarios.application.service;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        // Establecer valores por defecto según los requisitos
        user.setRol(RolUsuario.Tutorado);
        user.setActiveTutoringLimit(0);
        
        return userRepository.save(user);
    }
}