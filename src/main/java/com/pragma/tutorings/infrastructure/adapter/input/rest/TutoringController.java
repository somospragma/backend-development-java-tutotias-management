package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.shared.dto.OkResponseDto;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.input.CancelTutoringUseCase;
import com.pragma.tutorings.domain.port.input.CompleteTutoringUseCase;
import com.pragma.tutorings.domain.port.input.CreateTutoringUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CreateTutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.UpdateTutoringStatusDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tutorings")
@RequiredArgsConstructor
public class TutoringController {

    private final CreateTutoringUseCase createTutoringUseCase;
    private final CompleteTutoringUseCase completeTutoringUseCase;
    private final CancelTutoringUseCase cancelTutoringUseCase;
    private final TutoringDtoMapper tutoringDtoMapper;

    @PostMapping
    public ResponseEntity<OkResponseDto<TutoringDto>> createTutoring(@Valid @RequestBody CreateTutoringDto createTutoringDto) {
        Tutoring tutoring = createTutoringUseCase.createTutoring(
                createTutoringDto.getTutoringRequestId(),
                createTutoringDto.getTutorId(),
                createTutoringDto.getObjectives()
        );
        
        TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OkResponseDto.of("Tutoría creada exitosamente", tutoringDto));
    }
    
    @PatchMapping("/{tutoringId}/complete")
    public ResponseEntity<OkResponseDto<TutoringDto>> completeTutoring(
            @PathVariable String tutoringId,
            @Valid @RequestBody UpdateTutoringStatusDto updateDto) {
        
        Tutoring tutoring = completeTutoringUseCase.completeTutoring(tutoringId, updateDto.getUserId());
        TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OkResponseDto.of("Tutoría marcada como completada exitosamente", tutoringDto));
    }
    
    @PatchMapping("/{tutoringId}/cancel")
    public ResponseEntity<OkResponseDto<TutoringDto>> cancelTutoring(
            @PathVariable String tutoringId,
            @Valid @RequestBody UpdateTutoringStatusDto updateDto) {
        
        Tutoring tutoring = cancelTutoringUseCase.cancelTutoring(tutoringId, updateDto.getUserId(), updateDto.getComments());
        TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OkResponseDto.of("Tutoría cancelada exitosamente", tutoringDto));
    }
}