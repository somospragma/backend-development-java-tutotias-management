package com.pragma.tutorings_requests.infrastructure.adapter.input.rest;

import com.pragma.shared.dto.OkResponseDto;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.port.input.CreateTutoringRequestUseCase;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.CreateTutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.TutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.mapper.TutoringRequestDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tutoring-requests")
@RequiredArgsConstructor
@Slf4j
public class TutoringRequestController {

    private final CreateTutoringRequestUseCase createTutoringRequestUseCase;
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
}