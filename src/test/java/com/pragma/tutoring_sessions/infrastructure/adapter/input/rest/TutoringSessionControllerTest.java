package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.input.CreateTutoringSessionUseCase;
import com.pragma.tutoring_sessions.domain.port.input.UpdateTutoringSessionStatusUseCase;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.CreateTutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.TutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.UpdateTutoringSessionStatusDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.mapper.TutoringSessionDtoMapper;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TutoringSessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateTutoringSessionUseCase createTutoringSessionUseCase;

    @Mock
    private UpdateTutoringSessionStatusUseCase updateTutoringSessionStatusUseCase;

    @Mock
    private TutoringSessionDtoMapper mapper;

    @InjectMocks
    private TutoringSessionController controller;

    private ObjectMapper objectMapper;
    private String tutoringId;
    private String sessionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        tutoringId = UUID.randomUUID().toString();
        sessionId = UUID.randomUUID().toString();
    }

    @Test
    void createTutoringSession_Success() throws Exception {
        // Arrange
        String topicsCovered = "Java basics, OOP concepts";
        CreateTutoringSessionDto createDto = new CreateTutoringSessionDto(
                tutoringId, "2023-06-15T14:00:00", 60, "https://meet.example.com/session", topicsCovered);

        Tutoring tutoring = new Tutoring();
        tutoring.setId(tutoringId);

        TutoringSession createdSession = new TutoringSession();
        createdSession.setId(sessionId);
        createdSession.setTutoring(tutoring);
        createdSession.setDatetime(createDto.getDatetime());
        createdSession.setDurationMinutes(createDto.getDurationMinutes());
        createdSession.setLocationLink(createDto.getLocationLink());
        createdSession.setTopicsCovered(createDto.getTopicsCovered());
        createdSession.setSessionStatus(TutoringsSessionStatus.Programada);

        TutoringSessionDto responseDto = new TutoringSessionDto();
        responseDto.setId(sessionId);
        responseDto.setDatetime(createDto.getDatetime());
        responseDto.setDurationMinutes(createDto.getDurationMinutes());
        responseDto.setLocationLink(createDto.getLocationLink());
        responseDto.setTopicsCovered(createDto.getTopicsCovered());
        responseDto.setSessionStatus(TutoringsSessionStatus.Programada);

        when(createTutoringSessionUseCase.createTutoringSession(
                eq(tutoringId), 
                eq(createDto.getDatetime()), 
                eq(createDto.getDurationMinutes()), 
                eq(createDto.getLocationLink()),
                eq(createDto.getTopicsCovered())
        )).thenReturn(createdSession);
        
        when(mapper.toDto(createdSession)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tutoring-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Tutoring session created successfully"))
                .andExpect(jsonPath("$.data.id").value(sessionId))
                .andExpect(jsonPath("$.data.datetime").value(createDto.getDatetime()))
                .andExpect(jsonPath("$.data.durationMinutes").value(createDto.getDurationMinutes()))
                .andExpect(jsonPath("$.data.locationLink").value(createDto.getLocationLink()))
                .andExpect(jsonPath("$.data.topicsCovered").value(createDto.getTopicsCovered()))
                .andExpect(jsonPath("$.data.sessionStatus").value(TutoringsSessionStatus.Programada.toString()));
    }

    @Test
    void updateTutoringSessionStatus_Success() throws Exception {
        // Arrange
        String notes = "Session completed successfully";
        UpdateTutoringSessionStatusDto updateDto = new UpdateTutoringSessionStatusDto(TutoringsSessionStatus.Realizada, notes);

        TutoringSession updatedSession = new TutoringSession();
        updatedSession.setId(sessionId);
        updatedSession.setSessionStatus(TutoringsSessionStatus.Realizada);
        updatedSession.setNotes(notes);

        TutoringSessionDto responseDto = new TutoringSessionDto();
        responseDto.setId(sessionId);
        responseDto.setSessionStatus(TutoringsSessionStatus.Realizada);
        responseDto.setNotes(notes);

        when(updateTutoringSessionStatusUseCase.updateSessionStatus(
                eq(sessionId), 
                eq(TutoringsSessionStatus.Realizada),
                eq(notes)
        )).thenReturn(updatedSession);
        
        when(mapper.toDto(updatedSession)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tutoring-sessions/{id}/status", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tutoring session status updated successfully"))
                .andExpect(jsonPath("$.data.id").value(sessionId))
                .andExpect(jsonPath("$.data.sessionStatus").value(TutoringsSessionStatus.Realizada.toString()))
                .andExpect(jsonPath("$.data.notes").value(notes));
    }
}