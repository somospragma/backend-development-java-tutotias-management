package com.pragma.tutorings_requests.application.service;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TutoringRequestServiceTest {

    @Mock
    private TutoringRequestRepository tutoringRequestRepository;

    @InjectMocks
    private TutoringRequestService tutoringRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTutoringRequest_ShouldSetDefaultValuesAndSave() {
        // Arrange
        TutoringRequest request = new TutoringRequest();
        request.setId("test-id");
        request.setNeedsDescription("Necesito ayuda con Spring Boot");
        
        TutoringRequest savedRequest = new TutoringRequest();
        savedRequest.setId("test-id");
        savedRequest.setNeedsDescription("Necesito ayuda con Spring Boot");
        savedRequest.setRequestStatus(RequestStatus.Enviada);
        savedRequest.setRequestDate(new Date());
        
        when(tutoringRequestRepository.save(any(TutoringRequest.class))).thenReturn(savedRequest);
        
        // Act
        TutoringRequest result = tutoringRequestService.createTutoringRequest(request);
        
        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.Enviada, result.getRequestStatus());
        assertNotNull(result.getRequestDate());
        
        verify(tutoringRequestRepository, times(1)).save(any(TutoringRequest.class));
    }
}