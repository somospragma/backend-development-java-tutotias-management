package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.shared.dto.OkResponseDto;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.input.CreateTutoringUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CreateTutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tutorings")
@RequiredArgsConstructor
public class TutoringController {

    private final CreateTutoringUseCase createTutoringUseCase;
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
}