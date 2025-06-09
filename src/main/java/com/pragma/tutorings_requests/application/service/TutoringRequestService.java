package com.pragma.tutorings_requests.application.service;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.input.CreateTutoringRequestUseCase;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutoringRequestService implements CreateTutoringRequestUseCase {

    private final TutoringRequestRepository tutoringRequestRepository;

    @Override
    public TutoringRequest createTutoringRequest(TutoringRequest tutoringRequest) {
        try {
            log.info("Procesando solicitud de tutoría con ID: {}", tutoringRequest.getId());
            
            // Establecer la fecha actual
            tutoringRequest.setRequestDate(new Date());
            
            // Establecer el estado por defecto como Enviada
            tutoringRequest.setRequestStatus(RequestStatus.Enviada);
            
            TutoringRequest savedRequest = tutoringRequestRepository.save(tutoringRequest);
            log.info("Solicitud de tutoría guardada exitosamente con ID: {}", savedRequest.getId());
            
            return savedRequest;
        } catch (Exception e) {
            log.error("Error al crear la solicitud de tutoría: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la solicitud de tutoría", e);
        }
    }
}