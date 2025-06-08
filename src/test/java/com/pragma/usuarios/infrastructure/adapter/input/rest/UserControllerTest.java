package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.domain.port.input.FindUserByIdUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRequestDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;
    
    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Chapter chapter = new Chapter("1", "Java");
        testUser = new User();
        testUser.setId("1");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@pragma.com");
        testUser.setChapter(chapter);
        testUser.setRol(RolUsuario.Tutorado);
        testUser.setActiveTutoringLimit(0);

        testUserDto = new UserDto();
        testUserDto.setId("1");
        testUserDto.setFirstName("John");
        testUserDto.setLastName("Doe");
        testUserDto.setEmail("john.doe@pragma.com");
        testUserDto.setRol(RolUsuario.Tutorado);
        testUserDto.setActiveTutoringLimit(0);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnOk() {
        // Arrange
        when(findUserByIdUseCase.findUserById("1")).thenReturn(Optional.of(testUser));
        when(userDtoMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.getUserById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(findUserByIdUseCase.findUserById(anyString())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDto> response = userController.getUserById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}