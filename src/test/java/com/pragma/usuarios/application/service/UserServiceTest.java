package com.pragma.usuarios.application.service;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

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
    }

    @Test
    void createUser_ShouldSetDefaultValues() {
        // Arrange
        User inputUser = new User();
        inputUser.setFirstName("John");
        inputUser.setLastName("Doe");
        inputUser.setEmail("john.doe@pragma.com");
        inputUser.setChapter(testUser.getChapter());
        // No establecemos rol ni activeTutoringLimit para verificar que el servicio los establece
        
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.createUser(inputUser);

        // Assert
        assertNotNull(result);
        assertEquals(RolUsuario.Tutorado, result.getRol());
        assertEquals(0, result.getActiveTutoringLimit());
        
        // Verify that the service set the default values before saving
        verify(userRepository).save(argThat(user ->
            user.getRol() == RolUsuario.Tutorado && 
            user.getActiveTutoringLimit() == 0
        ));
    }
    
    @Test
    void findUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findUserById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals(testUser.getFirstName(), result.get().getFirstName());
        assertEquals(testUser.getLastName(), result.get().getLastName());
        
        // Verify repository was called with correct ID
        verify(userRepository).findById("1");
    }
    
    @Test
    void findUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserById("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        
        // Verify repository was called
        verify(userRepository).findById("nonexistent");
    }
}