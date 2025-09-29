package com.pragma.usuarios.application.service;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.output.ExternalUserRepository;
import com.pragma.usuarios.domain.port.output.UserRepository;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceCompleteTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TutoringRepository tutoringRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private ExternalUserRepository externalUserRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserWithTutoringCountDto testUserDto;

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
        testUser.setSeniority(3);

        testUserDto = new UserWithTutoringCountDto();
        testUserDto.setId("1");
        testUserDto.setFirstName("John");
        testUserDto.setLastName("Doe");
        testUserDto.setEmail("john.doe@pragma.com");
        testUserDto.setRol(RolUsuario.Tutorado);
        testUserDto.setActiveTutoringLimit(0);
        testUserDto.setSeniority(3);
        testUserDto.setTutoringsAsTutor(0L);
        testUserDto.setTutoringsAsTutee(1L);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAllowedFields() {
        // Arrange
        User updatedUserData = new User();
        updatedUserData.setFirstName("Jane");
        updatedUserData.setLastName("Smith");
        updatedUserData.setSeniority(5);
        
        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setFirstName("Jane");
        savedUser.setLastName("Smith");
        savedUser.setEmail("john.doe@pragma.com");
        savedUser.setChapter(testUser.getChapter());
        savedUser.setRol(RolUsuario.Tutorado);
        savedUser.setActiveTutoringLimit(0);
        savedUser.setSeniority(5);
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        Optional<User> result = userService.updateUser("1", updatedUserData);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        assertEquals(5, result.get().getSeniority());
        
        verify(userRepository).findById("1");
        verify(userRepository).save(argThat(user -> 
            "Jane".equals(user.getFirstName()) && 
            "Smith".equals(user.getLastName()) &&
            user.getSeniority() == 5
        ));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        User updatedUserData = new User();
        updatedUserData.setFirstName("Jane");
        
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.updateUser("nonexistent", updatedUserData);

        // Assert
        assertFalse(result.isPresent());
        
        verify(userRepository).findById("nonexistent");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsersWithTutoringCountFiltered_ShouldReturnFilteredUsers() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findByFilters("chapter-1", "TUTORADO", 3, "john@test.com"))
                .thenReturn(users);
        when(tutoringRepository.countTutoringsByTutorId("1")).thenReturn(0L);
        when(tutoringRepository.countTutoringsByTuteeId("1")).thenReturn(1L);
        when(userDtoMapper.toUserWithTutoringCountDto(testUser)).thenReturn(testUserDto);

        // Act
        List<UserWithTutoringCountDto> result = userService.getAllUsersWithTutoringCountFiltered(
                "chapter-1", "TUTORADO", 3, "john@test.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals(0L, result.get(0).getTutoringsAsTutor());
        assertEquals(1L, result.get(0).getTutoringsAsTutee());
        
        verify(userRepository).findByFilters("chapter-1", "TUTORADO", 3, "john@test.com");
        verify(tutoringRepository).countTutoringsByTutorId("1");
        verify(tutoringRepository).countTutoringsByTuteeId("1");
        verify(userDtoMapper).toUserWithTutoringCountDto(testUser);
    }

    @Test
    void getExternalUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Arrange
        PragmaUserDto externalUser = new PragmaUserDto();
        externalUser.setEmail("test@pragma.com");
        externalUser.setFullName("External User");
        
        when(externalUserRepository.findUserByEmail("test@pragma.com"))
                .thenReturn(Optional.of(externalUser));

        // Act
        Optional<PragmaUserDto> result = userService.getExternalUserByEmail("test@pragma.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@pragma.com", result.get().getEmail());
        assertEquals("External User", result.get().getFullName());
        
        verify(externalUserRepository).findUserByEmail("test@pragma.com");
    }

    @Test
    void getExternalUserByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(externalUserRepository.findUserByEmail("notfound@pragma.com"))
                .thenReturn(Optional.empty());

        // Act
        Optional<PragmaUserDto> result = userService.getExternalUserByEmail("notfound@pragma.com");

        // Assert
        assertFalse(result.isPresent());
        
        verify(externalUserRepository).findUserByEmail("notfound@pragma.com");
    }

    @Test
    void findUserByGoogleId_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByGoogleUserId("google-123")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findUserByGoogleId("google-123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("john.doe@pragma.com", result.get().getEmail());
        
        verify(userRepository).findByGoogleUserId("google-123");
    }

    @Test
    void findUserByGoogleId_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByGoogleUserId("nonexistent-google-id")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserByGoogleId("nonexistent-google-id");

        // Assert
        assertFalse(result.isPresent());
        
        verify(userRepository).findByGoogleUserId("nonexistent-google-id");
    }
}