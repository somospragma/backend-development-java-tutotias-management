package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest;

import com.pragma.shared.dto.OkResponseDto;
import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.input.CreateTutoringSessionUseCase;
import com.pragma.tutoring_sessions.domain.port.input.UpdateTutoringSessionStatusUseCase;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.CreateTutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.TutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.UpdateTutoringSessionStatusDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.mapper.TutoringSessionDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tutoring-sessions")
@RequiredArgsConstructor
public class TutoringSessionController {

    private final CreateTutoringSessionUseCase createTutoringSessionUseCase;
    private final UpdateTutoringSessionStatusUseCase updateTutoringSessionStatusUseCase;
    private final TutoringSessionDtoMapper mapper;

    @PostMapping
    public ResponseEntity<OkResponseDto<TutoringSessionDto>> createTutoringSession(
            @Valid @RequestBody CreateTutoringSessionDto createTutoringSessionDto) {
        
        TutoringSession tutoringSession = createTutoringSessionUseCase.createTutoringSession(
                createTutoringSessionDto.getTutoringId(),
                createTutoringSessionDto.getDatetime(),
                createTutoringSessionDto.getDurationMinutes(),
                createTutoringSessionDto.getLocationLink(),
                createTutoringSessionDto.getTopicsCovered()
        );
        
        TutoringSessionDto responseDto = mapper.toDto(tutoringSession);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OkResponseDto.of("Tutoring session created successfully", responseDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OkResponseDto<TutoringSessionDto>> updateTutoringSessionStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateTutoringSessionStatusDto updateStatusDto) {
        
        TutoringSession updatedSession = updateTutoringSessionStatusUseCase.updateSessionStatus(
                id, updateStatusDto.getStatus(), updateStatusDto.getNotes());
        
        TutoringSessionDto responseDto = mapper.toDto(updatedSession);
        return ResponseEntity.ok(OkResponseDto.of("Tutoring session status updated successfully", responseDto));
    }
}