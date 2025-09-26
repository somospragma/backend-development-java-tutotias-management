package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.shared.context.UserContext;
import com.pragma.shared.service.MessageService;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.tutorings.domain.port.input.GetTutoringsUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.chapter.domain.model.Chapter;
import com.pragma.skills.domain.model.Skill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TutoringController.class)
class TutoringControllerGetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTutoringsUseCase getTutoringsUseCase;

    @MockBean
    private TutoringDtoMapper tutoringDtoMapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private com.pragma.usuarios.application.service.UserService userService;

    @MockBean
    private com.pragma.shared.config.AuthenticationProperties authenticationProperties;

    @MockBean
    private com.pragma.shared.security.GoogleAuthInterceptor googleAuthInterceptor;

    @MockBean
    private com.pragma.tutorings.domain.port.input.CreateTutoringUseCase createTutoringUseCase;

    @MockBean
    private com.pragma.tutorings.domain.port.input.CompleteTutoringUseCase completeTutoringUseCase;

    @MockBean
    private com.pragma.tutorings.domain.port.input.CancelTutoringUseCase cancelTutoringUseCase;

    @MockBean
    private com.pragma.tutorings.domain.port.input.RequestCancellationUseCase requestCancellationUseCase;

    private User testUser;
    private Tutoring tutoring1;
    private Tutoring tutoring2;

    @BeforeEach
    void setUp() throws Exception {
        // Setup test user
        Chapter chapter = new Chapter("chapter-1", "Engineering");
        testUser = new User("user-1", "Test", "User", "test@example.com", 
                           "google-123", "slack-123", chapter, RolUsuario.Administrador, 5, 1);
        UserContext.setCurrentUser(testUser);
        
        // Mock the interceptor to always return true (allow request to proceed)
        when(googleAuthInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        
        // Mock UserService to return the test user
        when(userService.findUserByGoogleId("google-123")).thenReturn(java.util.Optional.of(testUser));

        // Setup test tutorings
        User tutor = new User("tutor-1", "Tutor", "One", "tutor@example.com", 
                             "google-tutor", "slack-tutor", chapter, RolUsuario.Tutor, 3, 2);
        User tutee = new User("tutee-1", "Tutee", "One", "tutee@example.com", 
                             "google-tutee", "slack-tutee", chapter, RolUsuario.Tutorado, 3, 1);
        
        Skill skill1 = new Skill("skill-1", "Java");
        Skill skill2 = new Skill("skill-2", "Spring Boot");

        tutoring1 = new Tutoring("tutoring-1", tutor, tutee, Arrays.asList(skill1), 
                                new Date(), new Date(), TutoringStatus.Activa, 
                                "Learn Java basics", null, new Date(), new Date());
        
        tutoring2 = new Tutoring("tutoring-2", tutor, tutee, Arrays.asList(skill2), 
                                new Date(), new Date(), TutoringStatus.Completada, 
                                "Learn Spring Boot", "http://example.com/act", new Date(), new Date());
    }

    @Test
    void getAllTutorings_Success() throws Exception {
        // Given
        List<Tutoring> tutorings = Arrays.asList(tutoring1, tutoring2);
        when(getTutoringsUseCase.getAllTutorings()).thenReturn(tutorings);
        when(messageService.getMessage("general.success")).thenReturn("Exitoso");

        // When & Then
        mockMvc.perform(get("/api/v1/tutorings")
                .header("Authorization", "google-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exitoso"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getAllTutorings_EmptyList() throws Exception {
        // Given
        when(getTutoringsUseCase.getAllTutorings()).thenReturn(Arrays.asList());
        when(messageService.getMessage("general.success")).thenReturn("Exitoso");

        // When & Then
        mockMvc.perform(get("/api/v1/tutorings")
                .header("Authorization", "google-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exitoso"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}