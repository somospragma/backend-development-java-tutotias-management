package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.shared.context.UserContextHelper;
import com.pragma.shared.dto.OkResponseDto;
import com.pragma.shared.service.MessageService;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.input.CancelTutoringUseCase;
import com.pragma.tutorings.domain.port.input.CompleteTutoringUseCase;
import com.pragma.tutorings.domain.port.input.CreateTutoringUseCase;
import com.pragma.tutorings.domain.port.input.GetTutoringsUseCase;
import com.pragma.tutorings.domain.port.input.RequestCancellationUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CompleteTutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CreateTutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.RequestCancellationDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDetailDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.UpdateTutoringStatusDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDetailDtoMapper;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import com.pragma.feedbacks.domain.port.input.GetFeedbacksUseCase;
import com.pragma.tutoring_sessions.domain.port.input.GetTutoringSessionsUseCase;
import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tutorings")
@RequiredArgsConstructor
@Slf4j
public class TutoringController {

    private final CreateTutoringUseCase createTutoringUseCase;
    private final CompleteTutoringUseCase completeTutoringUseCase;
    private final CancelTutoringUseCase cancelTutoringUseCase;
    private final RequestCancellationUseCase requestCancellationUseCase;
    private final GetTutoringsUseCase getTutoringsUseCase;
    private final GetFeedbacksUseCase getFeedbacksUseCase;
    private final GetTutoringSessionsUseCase getTutoringSessionsUseCase;
    private final TutoringDtoMapper tutoringDtoMapper;
    private final TutoringDetailDtoMapper tutoringDetailDtoMapper;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<OkResponseDto<TutoringDto>> createTutoring(@Valid @RequestBody CreateTutoringDto createTutoringDto) {
        User currentUser = UserContextHelper.getCurrentUserOrThrow();
        log.info("User {} creating tutoring for request: {} with tutor: {}", 
                currentUser.getEmail(), createTutoringDto.getTutoringRequestId(), createTutoringDto.getTutorId());
        
        // Only tutors and admins can create tutorings
        if (!UserContextHelper.canActAsTutor()) {
            log.warn("User {} attempted to create tutoring without tutor privileges", currentUser.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(OkResponseDto.of("No tiene permisos para crear tutorías", null));
        }
        
        Tutoring tutoring = createTutoringUseCase.createTutoring(
                createTutoringDto.getTutoringRequestId(),
                createTutoringDto.getTutorId(),
                createTutoringDto.getObjectives()
        );
        
        TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
        
        log.info("User {} successfully created tutoring with ID: {}", currentUser.getEmail(), tutoring.getId());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OkResponseDto.of("Tutoría creada exitosamente", tutoringDto));
    }
    
    @PatchMapping("/{tutoringId}/complete")
    public ResponseEntity<OkResponseDto<TutoringDto>> completeTutoring(
            @PathVariable String tutoringId,
            @Valid @RequestBody CompleteTutoringDto completeDto) {
        
        User currentUser = UserContextHelper.getCurrentUserOrThrow();
        log.info("User {} completing tutoring: {}", currentUser.getEmail(), tutoringId);
        
        // Validate that user can complete tutorings (tutors and admins)
        if (!UserContextHelper.canActAsTutor()) {
            log.warn("User {} attempted to complete tutoring without tutor privileges", currentUser.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(OkResponseDto.of("No tiene permisos para completar tutorías", null));
        }
        
        // Set the current user as the one completing the tutoring
        completeDto.setUserId(currentUser.getId());
        
        Tutoring tutoring = completeTutoringUseCase.completeTutoring(
                tutoringId, completeDto.getUserId(), completeDto.getFinalActUrl());
        TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
        
        log.info("User {} successfully completed tutoring: {}", currentUser.getEmail(), tutoringId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OkResponseDto.of("Tutoría marcada como completada exitosamente", tutoringDto));
    }
    
    @PatchMapping("/{tutoringId}/cancel")
    public ResponseEntity<OkResponseDto<TutoringDto>> cancelTutoring(
            @PathVariable String tutoringId,
            @Valid @RequestBody UpdateTutoringStatusDto updateDto) {
        
        User currentUser = UserContextHelper.getCurrentUserOrThrow();
        
        // If user is admin, proceed with cancellation
        if (UserContextHelper.isCurrentUserAdmin()) {
            log.info("Admin {} canceling tutoring: {} with reason: {}", 
                    currentUser.getEmail(), tutoringId, updateDto.getComments());
            
            updateDto.setUserId(currentUser.getId());
            
            Tutoring tutoring = cancelTutoringUseCase.cancelTutoring(
                    tutoringId, updateDto.getUserId(), updateDto.getComments());
            TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
            
            log.info("Admin {} successfully canceled tutoring: {}", currentUser.getEmail(), tutoringId);
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(OkResponseDto.of("Tutoría cancelada exitosamente", tutoringDto));
        } else {
            // If user is not admin, request cancellation
            log.info("User {} requesting cancellation for tutoring: {} with reason: {}", 
                    currentUser.getEmail(), tutoringId, updateDto.getComments());
            
            updateDto.setUserId(currentUser.getId());
            
            Tutoring tutoring = requestCancellationUseCase.requestCancellation(
                    tutoringId, updateDto.getUserId(), updateDto.getComments());
            TutoringDto tutoringDto = tutoringDtoMapper.toDto(tutoring);
            
            log.info("User {} successfully requested cancellation for tutoring: {}", currentUser.getEmail(), tutoringId);
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(OkResponseDto.of("Solicitud de cancelación enviada exitosamente", tutoringDto));
        }
    }
    
    @GetMapping
    public ResponseEntity<OkResponseDto<List<TutoringDto>>> getAllTutorings() {
        try {
            User currentUser = UserContextHelper.getCurrentUserOrThrow();
            log.info("User {} retrieving all tutorings", currentUser.getEmail());
            
            List<Tutoring> tutorings = getTutoringsUseCase.getAllTutorings();
            List<TutoringDto> tutoringDtos = tutoringDtoMapper.toDtoList(tutorings);
            
            log.info("User {} retrieved {} tutorings", currentUser.getEmail(), tutoringDtos.size());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(OkResponseDto.of(messageService.getMessage("general.success"), tutoringDtos));
        } catch (Exception e) {
            log.error("Error retrieving tutorings", e);
            throw e;
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<OkResponseDto<TutoringDetailDto>> getTutoringDetail(@PathVariable String id) {
        User currentUser = UserContextHelper.getCurrentUserOrThrow();
        log.info("User {} retrieving tutoring detail for ID: {}", currentUser.getEmail(), id);
        
        Tutoring tutoring = getTutoringsUseCase.getTutoringById(id);
        List<TutoringSession> sessions = getTutoringSessionsUseCase.getSessionsByTutoringId(id);
        List<Feedback> feedbacks = getFeedbacksUseCase.getFeedbacksByTutoringId(id);
        
        TutoringDetailDto detailDto = tutoringDetailDtoMapper.toDetailDto(tutoring, sessions, feedbacks);
        
        log.info("User {} retrieved tutoring detail with {} sessions and {} feedbacks", 
                currentUser.getEmail(), sessions.size(), feedbacks.size());
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OkResponseDto.of("Detalle de tutoría obtenido exitosamente", detailDto));
    }
}