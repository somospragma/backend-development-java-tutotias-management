package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.shared.context.UserContext;
import com.pragma.shared.context.UserContextHelper;
import com.pragma.shared.service.MessageService;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.CreateUserUseCase;
import com.pragma.usuarios.domain.port.input.GetExternalUserUseCase;
import com.pragma.usuarios.domain.port.input.UpdateUserUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRequestDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import org.junit.jupiter.api.AfterEach;
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

class UserControllerCompleteTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private GetExternalUserUseCase getExternalUserUseCase;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDto testUserDto;
    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        UserContextHelper.setMessageServiceForTesting(messageService);
        when(messageService.getMessage(anyString())).thenReturn("Test message");

        Chapter chapter = new Chapter("1", "Java");
        
        adminUser = new User();
        adminUser.setId("admin-1");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setEmail("admin@pragma.com");
        adminUser.setChapter(chapter);
        adminUser.setRol(RolUsuario.Administrador);
        adminUser.setActiveTutoringLimit(0);
        
        UserContext.setCurrentUser(adminUser);
        
        testUser = new User();
        testUser.setId("1");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@pragma.com");
        testUser.setChapter(chapter);
        testUser.setRol(RolUsuario.Tutorado);
        testUser.setActiveTutoringLimit(0);
        testUser.setSeniority(3);

        testUserDto = new UserDto();
        testUserDto.setId("1");
        testUserDto.setFirstName("John");
        testUserDto.setLastName("Doe");
        testUserDto.setEmail("john.doe@pragma.com");
        testUserDto.setRol(RolUsuario.Tutorado);
        testUserDto.setActiveTutoringLimit(0);
        testUserDto.setSeniority(3);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreated() {
        // Arrange
        CreateUserDto createUserDto = new CreateUserDto("john.doe@pragma.com", "google-123");
        
        PragmaUserDto externalUser = new PragmaUserDto();
        externalUser.setEmail("john.doe@pragma.com");
        externalUser.setFullName("John Doe");

        when(getExternalUserUseCase.getExternalUserByEmail("john.doe@pragma.com"))
                .thenReturn(Optional.of(externalUser));
        when(userDtoMapper.toModelFromExternal(createUserDto, externalUser)).thenReturn(testUser);
        when(createUserUseCase.createUser(any(User.class))).thenReturn(testUser);
        when(userDtoMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.createUser(createUserDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void updateUser_WithValidData_ShouldReturnOk() {
        // Arrange
        UpdateUserRequestDto updateDto = new UpdateUserRequestDto();
        updateDto.setId("1");
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Smith");
        updateDto.setSeniority(5);

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");
        updatedUser.setEmail("john.doe@pragma.com");
        updatedUser.setChapter(testUser.getChapter());
        updatedUser.setRol(RolUsuario.Tutorado);
        updatedUser.setActiveTutoringLimit(0);
        updatedUser.setSeniority(5);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId("1");
        updatedUserDto.setFirstName("Jane");
        updatedUserDto.setLastName("Smith");
        updatedUserDto.setEmail("john.doe@pragma.com");
        updatedUserDto.setRol(RolUsuario.Tutorado);
        updatedUserDto.setActiveTutoringLimit(0);
        updatedUserDto.setSeniority(5);

        when(userDtoMapper.toModel(updateDto)).thenReturn(updatedUser);
        when(updateUserUseCase.updateUser("1", updatedUser)).thenReturn(Optional.of(updatedUser));
        when(userDtoMapper.toDto(updatedUser)).thenReturn(updatedUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.updateUser(updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
        assertEquals("Smith", response.getBody().getLastName());
        assertEquals(5, response.getBody().getSeniority());
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldReturnNotFound() {
        // Arrange
        UpdateUserRequestDto updateDto = new UpdateUserRequestDto();
        updateDto.setId("nonexistent");
        updateDto.setFirstName("Jane");

        when(userDtoMapper.toModel(updateDto)).thenReturn(testUser);
        when(updateUserUseCase.updateUser("nonexistent", testUser)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDto> response = userController.updateUser(updateDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Note: getExternalUser method doesn't exist in UserController, removing these tests

    @Test
    void getCurrentUser_ShouldReturnCurrentUserFromContext() {
        // Arrange
        when(userDtoMapper.toDto(adminUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<com.pragma.shared.dto.OkResponseDto<UserDto>> response = userController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
    }
}