package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.usuarios.domain.port.input.GetAllUsersWithTutoringCountUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.chapter.infrastructure.adapter.input.rest.dto.ChapterDto;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.shared.context.UserContext;
import com.pragma.usuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("UserController - getAllUsersWithTutoringCount Filters Tests")
class UserControllerGetAllUsersFilterTest {

    @Mock
    private GetAllUsersWithTutoringCountUseCase getAllUsersWithTutoringCountUseCase;

    @InjectMocks
    private UserController userController;

    private List<UserWithTutoringCountDto> mockUsers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock admin user in context
        User adminUser = new User();
        adminUser.setId("admin-id");
        adminUser.setEmail("admin@test.com");
        adminUser.setRol(RolUsuario.Administrador);
        UserContext.setCurrentUser(adminUser);

        // Setup mock users
        ChapterDto engineeringChapter = new ChapterDto("chapter-1", "Engineering");
        ChapterDto marketingChapter = new ChapterDto("chapter-2", "Marketing");

        UserWithTutoringCountDto user1 = new UserWithTutoringCountDto();
        user1.setId("user-1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john@test.com");
        user1.setRol(RolUsuario.Tutor);
        user1.setChapter(engineeringChapter);
        user1.setSeniority(5);
        user1.setTutoringsAsTutor(3L);
        user1.setTutoringsAsTutee(1L);

        UserWithTutoringCountDto user2 = new UserWithTutoringCountDto();
        user2.setId("user-2");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane@test.com");
        user2.setRol(RolUsuario.Tutorado);
        user2.setChapter(marketingChapter);
        user2.setSeniority(3);
        user2.setTutoringsAsTutor(0L);
        user2.setTutoringsAsTutee(2L);

        mockUsers = Arrays.asList(user1, user2);
    }
    
    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("Should call getAllUsersWithTutoringCount when no filters provided")
    void getAllUsersWithTutoringCount_NoFilters_ShouldCallUnfilteredMethod() {
        // Arrange
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCount()).thenReturn(mockUsers);

        // Act
        var result = userController.getAllUsersWithTutoringCount(null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        
        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCount();
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCountFiltered(
                org.mockito.ArgumentMatchers.any(), 
                org.mockito.ArgumentMatchers.any(), 
                org.mockito.ArgumentMatchers.any(), 
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should filter by chapterId when provided")
    void getAllUsersWithTutoringCount_WithChapterIdFilter_ShouldCallFilteredMethod() {
        // Arrange
        String chapterId = "chapter-1";
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                chapterId, null, null, null)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(chapterId, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(chapterId, result.getBody().get(0).getChapter().getId());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                chapterId, null, null, null);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should filter by rol when provided")
    void getAllUsersWithTutoringCount_WithRolFilter_ShouldCallFilteredMethod() {
        // Arrange
        String rol = "TUTOR";
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, rol, null, null)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(null, rol, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(RolUsuario.Tutor, result.getBody().get(0).getRol());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, rol, null, null);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should filter by seniority when provided")
    void getAllUsersWithTutoringCount_WithSeniorityFilter_ShouldCallFilteredMethod() {
        // Arrange
        Integer seniority = 5;
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, null, seniority, null)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(null, null, seniority, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(seniority, result.getBody().get(0).getSeniority());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, null, seniority, null);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should filter by email when provided")
    void getAllUsersWithTutoringCount_WithEmailFilter_ShouldCallFilteredMethod() {
        // Arrange
        String email = "john@test.com";
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, null, null, email)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(null, null, null, email);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(email, result.getBody().get(0).getEmail());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, null, null, email);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should filter by multiple parameters when provided")
    void getAllUsersWithTutoringCount_WithMultipleFilters_ShouldCallFilteredMethod() {
        // Arrange
        String chapterId = "chapter-1";
        String rol = "TUTOR";
        Integer seniority = 5;
        String email = "john@test.com";
        
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                chapterId, rol, seniority, email)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(chapterId, rol, seniority, email);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        
        var user = result.getBody().get(0);
        assertEquals(chapterId, user.getChapter().getId());
        assertEquals(RolUsuario.Tutor, user.getRol());
        assertEquals(seniority, user.getSeniority());
        assertEquals(email, user.getEmail());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                chapterId, rol, seniority, email);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should filter by partial combination of parameters")
    void getAllUsersWithTutoringCount_WithPartialFilters_ShouldCallFilteredMethod() {
        // Arrange
        String chapterId = "chapter-1";
        String rol = "TUTOR";
        
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                chapterId, rol, null, null)).thenReturn(Arrays.asList(mockUsers.get(0)));

        // Act
        var result = userController.getAllUsersWithTutoringCount(chapterId, rol, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                chapterId, rol, null, null);
        verify(getAllUsersWithTutoringCountUseCase, never()).getAllUsersWithTutoringCount();
    }

    @Test
    @DisplayName("Should return empty list when no users match filters")
    void getAllUsersWithTutoringCount_WithFiltersNoMatches_ShouldReturnEmptyList() {
        // Arrange
        String chapterId = "non-existent-chapter";
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                chapterId, null, null, null)).thenReturn(Arrays.asList());

        // Act
        var result = userController.getAllUsersWithTutoringCount(chapterId, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(0, result.getBody().size());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                chapterId, null, null, null);
    }

    @Test
    @DisplayName("Should pass empty string parameters to use case")
    void getAllUsersWithTutoringCount_WithEmptyStringParameters_ShouldPassToUseCase() {
        // Arrange
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                "", "", null, "")).thenReturn(mockUsers);

        // Act
        var result = userController.getAllUsersWithTutoringCount("", "", null, "");

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());

        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                "", "", null, "");
    }
    
    @Test
    @DisplayName("Should verify all filter combinations work correctly")
    void getAllUsersWithTutoringCount_AllFilterCombinations_ShouldWork() {
        // Test that the controller correctly delegates to the filtered method
        // when any combination of parameters is provided
        
        // Test chapterId only
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                "chapter-1", null, null, null)).thenReturn(mockUsers);
        var result1 = userController.getAllUsersWithTutoringCount("chapter-1", null, null, null);
        assertEquals(200, result1.getStatusCodeValue());
        
        // Test rol only
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, "TUTOR", null, null)).thenReturn(mockUsers);
        var result2 = userController.getAllUsersWithTutoringCount(null, "TUTOR", null, null);
        assertEquals(200, result2.getStatusCodeValue());
        
        // Test seniority only
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, null, 5, null)).thenReturn(mockUsers);
        var result3 = userController.getAllUsersWithTutoringCount(null, null, 5, null);
        assertEquals(200, result3.getStatusCodeValue());
        
        // Test email only
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCountFiltered(
                null, null, null, "test@test.com")).thenReturn(mockUsers);
        var result4 = userController.getAllUsersWithTutoringCount(null, null, null, "test@test.com");
        assertEquals(200, result4.getStatusCodeValue());
        
        // Verify all calls were made to the filtered method
        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                "chapter-1", null, null, null);
        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, "TUTOR", null, null);
        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, null, 5, null);
        verify(getAllUsersWithTutoringCountUseCase).getAllUsersWithTutoringCountFiltered(
                null, null, null, "test@test.com");
    }
}