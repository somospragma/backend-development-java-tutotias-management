package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserController userController;

    private CreateUserDto createUserDto;
    private User user;
    private User createdUser;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createUserDto = new CreateUserDto(
            "John",
            "Doe",
            "john.doe@pragma.com",
            "1"
        );

        Chapter chapter = new Chapter("1", "Java");
        
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@pragma.com");
        user.setChapter(chapter);
        
        createdUser = new User();
        createdUser.setId("1");
        createdUser.setFirstName("John");
        createdUser.setLastName("Doe");
        createdUser.setEmail("john.doe@pragma.com");
        createdUser.setChapter(chapter);
        createdUser.setRol(RolUsuario.Tutorado);
        createdUser.setActiveTutoringLimit(0);
        
        userDto = new UserDto();
        userDto.setId("1");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@pragma.com");
        userDto.setRol(RolUsuario.Tutorado);
        userDto.setActiveTutoringLimit(0);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        when(userDtoMapper.toModel(createUserDto)).thenReturn(user);
        when(createUserUseCase.createUser(user)).thenReturn(createdUser);
        when(userDtoMapper.toDto(createdUser)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.createUser(createUserDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        assertEquals(RolUsuario.Tutorado, response.getBody().getRol());
        assertEquals(0, response.getBody().getActiveTutoringLimit());
    }
}