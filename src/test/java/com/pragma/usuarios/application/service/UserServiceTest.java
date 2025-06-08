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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@pragma.com");
        testUser.setChapter(chapter);
        // No establecemos rol ni activeTutoringLimit para verificar que el servicio los establece
    }

    @Test
    void createUser_ShouldSetDefaultValues() {
        // Arrange
        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setFirstName(testUser.getFirstName());
        savedUser.setLastName(testUser.getLastName());
        savedUser.setEmail(testUser.getEmail());
        savedUser.setChapter(testUser.getChapter());
        savedUser.setRol(RolUsuario.Tutorado);
        savedUser.setActiveTutoringLimit(0);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(testUser);

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
}