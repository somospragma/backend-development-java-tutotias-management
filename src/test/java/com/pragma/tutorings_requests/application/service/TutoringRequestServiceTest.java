package com.pragma.tutorings_requests.application.service;

import com.pragma.skills.domain.model.Skill;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import com.pragma.usuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutoringRequestServiceTest {

    @Mock
    private TutoringRequestRepository tutoringRequestRepository;

    @InjectMocks
    private TutoringRequestService tutoringRequestService;

    private TutoringRequest tutoringRequest;
    private String requestId;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID().toString();
        
        User tutee = new User();
        tutee.setId("user-id");
        
        Skill skill = new Skill();
        skill.setId("skill-id");
        skill.setName("Java");
        
        tutoringRequest = new TutoringRequest();
        tutoringRequest.setId(requestId);
        tutoringRequest.setTutee(tutee);
        tutoringRequest.setSkills(Arrays.asList(skill));
        tutoringRequest.setNeedsDescription("Necesito ayuda con Java");
        tutoringRequest.setRequestDate(new Date());
        tutoringRequest.setRequestStatus(RequestStatus.Enviada);
    }

    @Test
    void createTutoringRequest_Success() {
        // Arrange
        when(tutoringRequestRepository.save(any(TutoringRequest.class))).thenReturn(tutoringRequest);

        // Act
        TutoringRequest result = tutoringRequestService.createTutoringRequest(tutoringRequest);

        // Assert
        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals(RequestStatus.Enviada, result.getRequestStatus());
        verify(tutoringRequestRepository, times(1)).save(any(TutoringRequest.class));
    }

    @Test
    void updateStatus_ToApproved_Success() {
        // Arrange
        when(tutoringRequestRepository.findById(requestId)).thenReturn(Optional.of(tutoringRequest));
        when(tutoringRequestRepository.save(any(TutoringRequest.class))).thenAnswer(invocation -> {
            TutoringRequest savedRequest = invocation.getArgument(0);
            assertEquals(RequestStatus.Aprobada, savedRequest.getRequestStatus());
            return savedRequest;
        });

        // Act
        TutoringRequest result = tutoringRequestService.updateStatus(requestId, RequestStatus.Aprobada);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.Aprobada, result.getRequestStatus());
        verify(tutoringRequestRepository, times(1)).findById(requestId);
        verify(tutoringRequestRepository, times(1)).save(any(TutoringRequest.class));
    }

    @Test
    void updateStatus_ToRejected_Success() {
        // Arrange
        when(tutoringRequestRepository.findById(requestId)).thenReturn(Optional.of(tutoringRequest));
        when(tutoringRequestRepository.save(any(TutoringRequest.class))).thenAnswer(invocation -> {
            TutoringRequest savedRequest = invocation.getArgument(0);
            assertEquals(RequestStatus.Rechazada, savedRequest.getRequestStatus());
            return savedRequest;
        });

        // Act
        TutoringRequest result = tutoringRequestService.updateStatus(requestId, RequestStatus.Rechazada);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.Rechazada, result.getRequestStatus());
        verify(tutoringRequestRepository, times(1)).findById(requestId);
        verify(tutoringRequestRepository, times(1)).save(any(TutoringRequest.class));
    }

    @Test
    void updateStatus_InvalidStatus_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            tutoringRequestService.updateStatus(requestId, RequestStatus.Asignada)
        );
        
        assertEquals("El estado solo puede ser actualizado a Aprobada o Rechazada", exception.getMessage());
        verify(tutoringRequestRepository, never()).save(any(TutoringRequest.class));
    }

    @Test
    void updateStatus_RequestNotFound_ThrowsException() {
        // Arrange
        when(tutoringRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            tutoringRequestService.updateStatus(requestId, RequestStatus.Aprobada)
        );
        
        assertTrue(exception.getMessage().contains("Solicitud de tutoría no encontrada"));
        verify(tutoringRequestRepository, never()).save(any(TutoringRequest.class));
    }

    @Test
    void updateStatus_RequestNotInSentStatus_ThrowsException() {
        // Arrange
        tutoringRequest.setRequestStatus(RequestStatus.Aprobada);
        when(tutoringRequestRepository.findById(requestId)).thenReturn(Optional.of(tutoringRequest));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            tutoringRequestService.updateStatus(requestId, RequestStatus.Rechazada)
        );
        
        assertEquals("Solo se pueden actualizar solicitudes en estado Enviada", exception.getMessage());
        verify(tutoringRequestRepository, never()).save(any(TutoringRequest.class));
    }
}