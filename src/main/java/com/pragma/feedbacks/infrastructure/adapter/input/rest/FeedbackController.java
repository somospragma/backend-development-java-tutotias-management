package com.pragma.feedbacks.infrastructure.adapter.input.rest;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.input.CreateFeedbackUseCase;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.dto.CreateFeedbackDto;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.dto.FeedbackDto;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.mapper.FeedbackDtoMapper;
import com.pragma.shared.dto.OkResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;
    private final FeedbackDtoMapper feedbackDtoMapper;

    @PostMapping
    public ResponseEntity<OkResponseDto<FeedbackDto>> createFeedback(@Valid @RequestBody CreateFeedbackDto createFeedbackDto) {
        Feedback feedback = feedbackDtoMapper.toDomain(createFeedbackDto);
        Feedback createdFeedback = createFeedbackUseCase.createFeedback(feedback);
        FeedbackDto feedbackDto = feedbackDtoMapper.toDto(createdFeedback);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OkResponseDto.of("Feedback creado exitosamente", feedbackDto));
    }
}