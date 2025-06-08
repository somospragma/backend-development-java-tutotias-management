package com.pragma.usuarios.application.service;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.port.input.UpdateUserUseCase;
import com.pragma.usuarios.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Optional<User> updateUser(String id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Actualizar solo los campos permitidos
                    if (updatedUser.getFirstName() != null) {
                        existingUser.setFirstName(updatedUser.getFirstName());
                    }
                    if (updatedUser.getLastName() != null) {
                        existingUser.setLastName(updatedUser.getLastName());
                    }
                    if (updatedUser.getChapter() != null) {
                        existingUser.setChapter(updatedUser.getChapter());
                    }
                    
                    // Mantener los campos que no deben actualizarse
                    // email, rol y activeTutoringLimit se omiten intencionalmente
                    
                    return userRepository.save(existingUser);
                });
    }
}