package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.domain.port.input.FindUserByIdUseCase;
import com.pragma.usuarios.domain.port.input.UpdateTutoringLimitUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserRoleUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateTutoringLimitDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRequestDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRoleDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UpdateUserRoleUseCase updateUserRoleUseCase;
    private final UpdateTutoringLimitUseCase updateTutoringLimitUseCase;
    private final UserDtoMapper userDtoMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        User user = userDtoMapper.toModel(createUserDto);
        User createdUser = createUserUseCase.createUser(user);
        return new ResponseEntity<>(userDtoMapper.toDto(createdUser), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequestDto requestDto) {
        User userToUpdate = userDtoMapper.toModel(requestDto);

        return updateUserUseCase.updateUser(requestDto.getId(), userToUpdate)
                .map(updatedUser -> ResponseEntity.ok(userDtoMapper.toDto(updatedUser)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return findUserByIdUseCase.findUserById(id)
                .map(user -> ResponseEntity.ok(userDtoMapper.toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/role")
    public ResponseEntity<UserDto> updateUserRole(@Valid @RequestBody UpdateUserRoleDto requestDto) {
        return updateUserRoleUseCase.updateUserRole(requestDto.getId(), requestDto.getRole())
                .map(updatedUser -> ResponseEntity.ok(userDtoMapper.toDto(updatedUser)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/tutoring-limit")
    public ResponseEntity<UserDto> updateTutoringLimit(@Valid @RequestBody UpdateTutoringLimitDto requestDto) {
        return updateTutoringLimitUseCase.updateTutoringLimit(
                requestDto.getId(), 
                requestDto.getActiveTutoringLimit())
                .map(updatedUser -> ResponseEntity.ok(userDtoMapper.toDto(updatedUser)))
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}