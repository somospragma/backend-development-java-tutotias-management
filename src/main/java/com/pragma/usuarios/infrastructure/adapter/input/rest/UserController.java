package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserDtoMapper userDtoMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        User user = userDtoMapper.toModel(createUserDto);
        User createdUser = createUserUseCase.createUser(user);
        return new ResponseEntity<>(userDtoMapper.toDto(createdUser), HttpStatus.CREATED);
    }
}