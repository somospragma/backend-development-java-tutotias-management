package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.input.GetFeedbacksUseCase;
import com.pragma.shared.context.UserContext;
import com.pragma.shared.service.MessageService;
import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.input.GetTutoringSessionsUseCase;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.tutorings.domain.port.input.GetTutoringsUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDetailDtoMapper;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TutoringController.class)
class TutoringDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTutoringsUseCase getTutoringsUseCase;

    @MockBean
    private GetFeedbacksUseCase getFeedbacksUseCase;

    @MockBean
    private GetTutoringSessionsUseCase getTutoringSessionsUseCase;

    @MockBean
    private TutoringDtoMapper tutoringDtoMapper;

    @MockBean
    private TutoringDetailDtoMapper tutoringDetailDtoMapper;

    @MockBean
    private MessageService messageService;

    private User testUser;
    private Tutoring testTutoring;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-id");
        testUser.setEmail("test@example.com");
        testUser.setRol(RolUsuario.Administrador);

        testTutoring = new Tutoring();
        testTutoring.setId("tutoring-id");
        testTutoring.setStatus(TutoringStatus.Activa);
        testTutoring.setCreatedAt(new Date());

        UserContext.setCurrentUser(testUser);
    }

    @Test
    void getTutoringDetail_Success() throws Exception {
        // Arrange
        List<TutoringSession> sessions = new ArrayList<>();
        List<Feedback> feedbacks = new ArrayList<>();

        when(getTutoringsUseCase.getTutoringById(anyString())).thenReturn(testTutoring);
        when(getTutoringSessionsUseCase.getSessionsByTutoringId(anyString())).thenReturn(sessions);
        when(getFeedbacksUseCase.getFeedbacksByTutoringId(anyString())).thenReturn(feedbacks);
        when(tutoringDetailDtoMapper.toDetailDto(any(), any(), any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tutorings/tutoring-id/detail")
                        .header("Authorization", "test-google-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Detalle de tutoría obtenido exitosamente"));
    }
}