package com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.entity.TutoringSessionsEntity;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.mapper.TutoringSessionMapper;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.repository.SpringDataTutoringSessionRepository;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.TutoringEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.TutoringMapper;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TutoringSessionPersistenceAdapterTest {

    @Mock
    private SpringDataTutoringSessionRepository repository;

    @Mock
    private TutoringSessionMapper mapper;

    @InjectMocks
    private TutoringSessionPersistenceAdapter adapter;

    private String sessionId;
    private String tutoringId;
    private TutoringSession tutoringSession;
    private TutoringSessionsEntity tutoringSessionsEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        sessionId = UUID.randomUUID().toString();
        tutoringId = UUID.randomUUID().toString();

        Tutoring tutoring = new Tutoring();
        tutoring.setId(tutoringId);

        TutoringEntity tutoringEntity = new TutoringEntity();
        tutoringEntity.setId(tutoringId);
        
        tutoringSession = new TutoringSession();
        tutoringSession.setId(sessionId);
        tutoringSession.setTutoring(tutoring);
        tutoringSession.setDatetime("2023-06-15T14:00:00");
        tutoringSession.setDurationMinutes(60);
        tutoringSession.setSessionStatus(TutoringsSessionStatus.Programada);
        
        tutoringSessionsEntity = new TutoringSessionsEntity();
        tutoringSessionsEntity.setId(sessionId);
        tutoringSessionsEntity.setTutoringId(tutoringEntity);
        tutoringSessionsEntity.setDatetime("2023-06-15T14:00:00");
        tutoringSessionsEntity.setDurationMinutes(60);
        tutoringSessionsEntity.setSessionStatus(TutoringsSessionStatus.Programada);
    }

    @Test
    void save_Success() {
        // Arrange
        when(mapper.toEntity(tutoringSession)).thenReturn(tutoringSessionsEntity);
        when(repository.save(tutoringSessionsEntity)).thenReturn(tutoringSessionsEntity);
        when(mapper.toModel(tutoringSessionsEntity)).thenReturn(tutoringSession);

        // Act
        TutoringSession result = adapter.save(tutoringSession);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        verify(mapper).toEntity(tutoringSession);
        verify(repository).save(tutoringSessionsEntity);
        verify(mapper).toModel(tutoringSessionsEntity);
    }

    @Test
    void findById_Success() {
        // Arrange
        when(repository.findById(sessionId)).thenReturn(Optional.of(tutoringSessionsEntity));
        when(mapper.toModel(tutoringSessionsEntity)).thenReturn(tutoringSession);

        // Act
        Optional<TutoringSession> result = adapter.findById(sessionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getId());
        verify(repository).findById(sessionId);
        verify(mapper).toModel(tutoringSessionsEntity);
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        // Act
        Optional<TutoringSession> result = adapter.findById(sessionId);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById(sessionId);
        verify(mapper, never()).toModel(any());
    }

    @Test
    void findByTutoringId_Success() {
        // Arrange
        List<TutoringSessionsEntity> entities = Arrays.asList(tutoringSessionsEntity);
        when(repository.findByTutoringId(any(TutoringEntity.class))).thenReturn(entities);
        when(mapper.toModel(tutoringSessionsEntity)).thenReturn(tutoringSession);

        // Act
        List<TutoringSession> result = adapter.findByTutoringId(tutoringId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sessionId, result.get(0).getId());
        verify(repository).findByTutoringId(any(TutoringEntity.class));
        verify(mapper).toModel(tutoringSessionsEntity);
    }

    @Test
    void updateStatus_Success() {
        // Arrange
        String notes = "Session completed successfully";
        TutoringSessionsEntity updatedEntity = new TutoringSessionsEntity();
        updatedEntity.setId(sessionId);
        updatedEntity.setSessionStatus(TutoringsSessionStatus.Realizada);
        updatedEntity.setNotes(notes);
        
        TutoringSession updatedSession = new TutoringSession();
        updatedSession.setId(sessionId);
        updatedSession.setSessionStatus(TutoringsSessionStatus.Realizada);
        updatedSession.setNotes(notes);
        
        when(repository.findById(sessionId)).thenReturn(Optional.of(tutoringSessionsEntity));
        when(repository.save(tutoringSessionsEntity)).thenReturn(updatedEntity);
        when(mapper.toModel(updatedEntity)).thenReturn(updatedSession);

        // Act
        TutoringSession result = adapter.updateStatus(sessionId, TutoringsSessionStatus.Realizada, notes);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals(TutoringsSessionStatus.Realizada, result.getSessionStatus());
        assertEquals(notes, result.getNotes());
        verify(repository).findById(sessionId);
        verify(repository).save(tutoringSessionsEntity);
        verify(mapper).toModel(updatedEntity);
    }

    @Test
    void updateStatus_NotFound() {
        // Arrange
        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            adapter.updateStatus(sessionId, TutoringsSessionStatus.Realizada, "Some notes")
        );
        
        assertEquals("Tutoring session not found with id: " + sessionId, exception.getMessage());
        verify(repository).findById(sessionId);
        verify(repository, never()).save(any());
        verify(mapper, never()).toModel(any());
    }
}