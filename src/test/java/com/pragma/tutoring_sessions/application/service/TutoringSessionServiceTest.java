package com.pragma.tutoring_sessions.application.service;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.output.TutoringSessionRepository;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TutoringSessionServiceTest {

    @Mock
    private TutoringSessionRepository tutoringSessionRepository;

    @Mock
    private TutoringRepository tutoringRepository;

    @InjectMocks
    private TutoringSessionService tutoringSessionService;

    private String tutoringId;
    private String sessionId;
    private Tutoring tutoring;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tutoringId = UUID.randomUUID().toString();
        sessionId = UUID.randomUUID().toString();
        tutoring = new Tutoring();
        tutoring.setId(tutoringId);
    }

    @Test
    void createTutoringSession_Success() {
        // Arrange
        String datetime = "2023-06-15T14:00:00";
        int durationMinutes = 60;
        String locationLink = "https://meet.example.com/session";
        String topicsCovered = "Java basics, OOP concepts";
        
        when(tutoringRepository.findById(tutoringId)).thenReturn(Optional.of(tutoring));
        
        TutoringSession savedSession = new TutoringSession();
        savedSession.setId(sessionId);
        savedSession.setTutoring(tutoring);
        savedSession.setDatetime(datetime);
        savedSession.setDurationMinutes(durationMinutes);
        savedSession.setLocationLink(locationLink);
        savedSession.setTopicsCovered(topicsCovered);
        savedSession.setSessionStatus(TutoringsSessionStatus.Programada);
        
        when(tutoringSessionRepository.save(any(TutoringSession.class))).thenReturn(savedSession);

        // Act
        TutoringSession result = tutoringSessionService.createTutoringSession(tutoringId, datetime, durationMinutes, locationLink, topicsCovered);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals(tutoring, result.getTutoring());
        assertEquals(datetime, result.getDatetime());
        assertEquals(durationMinutes, result.getDurationMinutes());
        assertEquals(locationLink, result.getLocationLink());
        assertEquals(topicsCovered, result.getTopicsCovered());
        assertEquals(TutoringsSessionStatus.Programada, result.getSessionStatus());
        
        verify(tutoringRepository).findById(tutoringId);
        verify(tutoringSessionRepository).save(any(TutoringSession.class));
    }

    @Test
    void createTutoringSession_TutoringNotFound() {
        // Arrange
        when(tutoringRepository.findById(tutoringId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            tutoringSessionService.createTutoringSession(tutoringId, "2023-06-15T14:00:00", 60, "https://meet.example.com/session", "Java basics")
        );
        
        assertEquals("Tutoring not found with id: " + tutoringId, exception.getMessage());
        verify(tutoringRepository).findById(tutoringId);
        verify(tutoringSessionRepository, never()).save(any(TutoringSession.class));
    }

    @Test
    void updateSessionStatus_Success() {
        // Arrange
        TutoringSession existingSession = new TutoringSession();
        existingSession.setId(sessionId);
        existingSession.setSessionStatus(TutoringsSessionStatus.Programada);
        
        when(tutoringSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
        
        TutoringSession updatedSession = new TutoringSession();
        updatedSession.setId(sessionId);
        updatedSession.setSessionStatus(TutoringsSessionStatus.Realizada);
        updatedSession.setNotes("Session completed successfully");
        
        String notes = "Session completed successfully";
        when(tutoringSessionRepository.updateStatus(sessionId, TutoringsSessionStatus.Realizada, notes)).thenReturn(updatedSession);

        // Act
        TutoringSession result = tutoringSessionService.updateSessionStatus(sessionId, TutoringsSessionStatus.Realizada, notes);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals(TutoringsSessionStatus.Realizada, result.getSessionStatus());
        assertEquals(notes, result.getNotes());
        
        verify(tutoringSessionRepository).findById(sessionId);
        verify(tutoringSessionRepository).updateStatus(sessionId, TutoringsSessionStatus.Realizada, notes);
    }

    @Test
    void updateSessionStatus_SessionNotFound() {
        // Arrange
        when(tutoringSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            tutoringSessionService.updateSessionStatus(sessionId, TutoringsSessionStatus.Realizada, "Some notes")
        );
        
        assertEquals("Tutoring session not found with id: " + sessionId, exception.getMessage());
        verify(tutoringSessionRepository).findById(sessionId);
        verify(tutoringSessionRepository, never()).updateStatus(any(), any(), any());
    }
}