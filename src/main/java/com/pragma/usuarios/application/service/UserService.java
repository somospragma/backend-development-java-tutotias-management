package com.pragma.usuarios.application.service;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.domain.port.input.FindUserByIdUseCase;
import com.pragma.usuarios.domain.port.input.FindUserByGoogleIdUseCase;
import com.pragma.usuarios.domain.port.input.GetAllUsersWithTutoringCountUseCase;
import com.pragma.usuarios.domain.port.input.GetExternalUserUseCase;
import com.pragma.usuarios.domain.port.input.UpdateTutoringLimitUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserRoleUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserUseCase;
import com.pragma.usuarios.domain.port.output.ExternalUserRepository;
import com.pragma.usuarios.domain.port.output.UserRepository;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, UpdateUserUseCase, FindUserByIdUseCase, 
        FindUserByGoogleIdUseCase, UpdateUserRoleUseCase, UpdateTutoringLimitUseCase, GetAllUsersWithTutoringCountUseCase, GetExternalUserUseCase {

    private final UserRepository userRepository;
    private final TutoringRepository tutoringRepository;
    private final UserDtoMapper userDtoMapper;
    private final ExternalUserRepository externalUserRepository;

    @Override
    public User createUser(User user) {
        // Establecer valores por defecto según los requisitos
        user.setRol(RolUsuario.Tutorado);
        user.setActiveTutoringLimit(0);
        
        return userRepository.save(user);
    }

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
                    if (updatedUser.getSlackId() != null) {
                        existingUser.setSlackId(updatedUser.getSlackId());
                    }
                    if (updatedUser.getSeniority() > 0) {
                        existingUser.setSeniority(updatedUser.getSeniority());
                    }

                    // Mantener los campos que no deben actualizarse
                    // email, rol y activeTutoringLimit se omiten intencionalmente

                    return userRepository.save(existingUser);
                });
    }
    
    @Override
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> updateUserRole(String id, RolUsuario role) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setRol(role);
                    return userRepository.save(existingUser);
                });
    }
    
    @Override
    public Optional<User> updateTutoringLimit(String id, int activeTutoringLimit) {
        return userRepository.findById(id)
                .filter(existingUser -> existingUser.getRol() == RolUsuario.Tutor)
                .map(existingUser -> {
                    existingUser.setActiveTutoringLimit(activeTutoringLimit);
                    return userRepository.save(existingUser);
                });
    }
    
    @Override
    public Optional<User> findUserByGoogleId(String googleUserId) {
        return userRepository.findByGoogleUserId(googleUserId);
    }

    @Override
    public List<UserWithTutoringCountDto> getAllUsersWithTutoringCount() {
        return userRepository.findAll().stream()
                .map(user -> {
                    long tutorCount = tutoringRepository.countTutoringsByTutorId(user.getId());
                    long tuteeCount = tutoringRepository.countTutoringsByTuteeId(user.getId());
                    
                    UserWithTutoringCountDto dto = userDtoMapper.toUserWithTutoringCountDto(user);
                    dto.setTutoringsAsTutor(tutorCount);
                    dto.setTutoringsAsTutee(tuteeCount);
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<UserWithTutoringCountDto> getAllUsersWithTutoringCountFiltered(String chapterId, String rol, Integer seniority, String email) {
        return userRepository.findByFilters(chapterId, rol, seniority, email).stream()
                .map(user -> {
                    long tutorCount = tutoringRepository.countTutoringsByTutorId(user.getId());
                    long tuteeCount = tutoringRepository.countTutoringsByTuteeId(user.getId());
                    
                    UserWithTutoringCountDto dto = userDtoMapper.toUserWithTutoringCountDto(user);
                    dto.setTutoringsAsTutor(tutorCount);
                    dto.setTutoringsAsTutee(tuteeCount);
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<PragmaUserDto> getExternalUserByEmail(String email) {
        return externalUserRepository.findUserByEmail(email);
    }
}