package com.pragma.tutorings_requests.application.service;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.input.CreateTutoringRequestUseCase;
import com.pragma.tutorings_requests.domain.port.input.UpdateTutoringRequestStatusUseCase;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutoringRequestService implements CreateTutoringRequestUseCase, UpdateTutoringRequestStatusUseCase {

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
    
    @Override
    public TutoringRequest updateStatus(String requestId, RequestStatus newStatus) {
        try {
            log.info("Actualizando estado de solicitud de tutoría con ID: {} a estado: {}", requestId, newStatus);
            
            // Validar que el nuevo estado sea válido (solo Aprobada o Rechazada)
            if (newStatus != RequestStatus.Aprobada && newStatus != RequestStatus.Rechazada) {
                throw new IllegalArgumentException("El estado solo puede ser actualizado a Aprobada o Rechazada");
            }
            
            // Buscar la solicitud por ID
            TutoringRequest tutoringRequest = tutoringRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Solicitud de tutoría no encontrada con ID: " + requestId));
            
            // Validar que la solicitud esté en estado Enviada
            if (tutoringRequest.getRequestStatus() != RequestStatus.Enviada) {
                throw new IllegalStateException("Solo se pueden actualizar solicitudes en estado Enviada");
            }
            
            // Actualizar el estado
            tutoringRequest.setRequestStatus(newStatus);
            
            // Guardar los cambios
            TutoringRequest updatedRequest = tutoringRequestRepository.save(tutoringRequest);
            log.info("Estado de solicitud de tutoría actualizado exitosamente a: {}", newStatus);
            
            return updatedRequest;
        } catch (Exception e) {
            log.error("Error al actualizar el estado de la solicitud de tutoría: {}", e.getMessage(), e);
            throw e;
        }
    }
}