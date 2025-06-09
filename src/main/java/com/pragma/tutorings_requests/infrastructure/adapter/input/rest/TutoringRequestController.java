package com.pragma.tutorings_requests.infrastructure.adapter.input.rest;

import com.pragma.shared.dto.OkResponseDto;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.port.input.CreateTutoringRequestUseCase;
import com.pragma.tutorings_requests.domain.port.input.GetTutoringRequestsUseCase;
import com.pragma.tutorings_requests.domain.port.input.UpdateTutoringRequestStatusUseCase;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.CreateTutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.TutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.TutoringRequestFilterDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.UpdateTutoringRequestStatusDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.mapper.TutoringRequestDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tutoring-requests")
@RequiredArgsConstructor
@Slf4j
public class TutoringRequestController {

    private final CreateTutoringRequestUseCase createTutoringRequestUseCase;
    private final UpdateTutoringRequestStatusUseCase updateTutoringRequestStatusUseCase;
    private final GetTutoringRequestsUseCase getTutoringRequestsUseCase;
    private final TutoringRequestDtoMapper tutoringRequestDtoMapper;

    @PostMapping
    public ResponseEntity<OkResponseDto<TutoringRequestDto>> createTutoringRequest(
            @Valid @RequestBody CreateTutoringRequestDto createTutoringRequestDto) {
        
        try {
            log.info("Creando solicitud de tutoría: {}", createTutoringRequestDto);
            
            TutoringRequest tutoringRequest = tutoringRequestDtoMapper.toModel(createTutoringRequestDto);

            TutoringRequest createdRequest = createTutoringRequestUseCase.createTutoringRequest(tutoringRequest);
            TutoringRequestDto responseDto = tutoringRequestDtoMapper.toDto(createdRequest);
            
            log.info("Solicitud de tutoría creada con ID: {}", createdRequest.getId());
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(OkResponseDto.of("Solicitud de tutoría creada exitosamente", responseDto));
        } catch (Exception e) {
            log.error("Error al crear solicitud de tutoría", e);
            throw e;
        }
    }
    
    @PatchMapping("/{requestId}/status")
    public ResponseEntity<OkResponseDto<TutoringRequestDto>> updateTutoringRequestStatus(
            @PathVariable String requestId,
            @Valid @RequestBody UpdateTutoringRequestStatusDto updateStatusDto) {
        
        try {
            log.info("Actualizando estado de solicitud de tutoría con ID: {} a estado: {}", 
                    requestId, updateStatusDto.getStatus());
            
            TutoringRequest updatedRequest = updateTutoringRequestStatusUseCase
                    .updateStatus(requestId, updateStatusDto.getStatus());
            
            TutoringRequestDto responseDto = tutoringRequestDtoMapper.toDto(updatedRequest);
            
            log.info("Estado de solicitud de tutoría actualizado exitosamente a: {}", 
                    updatedRequest.getRequestStatus());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(OkResponseDto.of("Estado de solicitud de tutoría actualizado exitosamente", responseDto));
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error de validación al actualizar estado de solicitud: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar estado de solicitud de tutoría", e);
            throw e;
        }
    }
    
    @GetMapping
    public ResponseEntity<OkResponseDto<List<TutoringRequestDto>>> getTutoringRequests(
            TutoringRequestFilterDto filterDto) {
        
        try {
            log.info("Obteniendo solicitudes de tutoría con filtros: {}", filterDto);
            
            List<TutoringRequest> requests;
            
            if (filterDto == null) {
                requests = getTutoringRequestsUseCase.getAllTutoringRequests();
            } else {
                requests = getTutoringRequestsUseCase.getTutoringRequestsWithFilters(
                        filterDto.getTuteeId(), 
                        filterDto.getSkillId(), 
                        filterDto.getStatus());
            }
            
            List<TutoringRequestDto> responseDtos = requests.stream()
                    .map(tutoringRequestDtoMapper::toDto)
                    .collect(Collectors.toList());
            
            log.info("Se encontraron {} solicitudes de tutoría", responseDtos.size());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(OkResponseDto.of("Solicitudes de tutoría obtenidas exitosamente", responseDtos));
        } catch (Exception e) {
            log.error("Error al obtener solicitudes de tutoría", e);
            throw e;
        }
    }
}