package com.pragma.feedbacks.application.service;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.input.CreateFeedbackUseCase;
import com.pragma.feedbacks.domain.port.output.FeedbackRepository;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService implements CreateFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final TutoringRepository tutoringRepository;
    private final UserRepository userRepository;

    @Override
    public Feedback createFeedback(Feedback feedback) {
        log.info("Creando feedback para la tutoría con ID: {}", feedback.getTutoring().getId());
        
        // Validar que la tutoría existe y cargar sus datos completos
        Tutoring tutoring = tutoringRepository.findById(feedback.getTutoring().getId())
            .orElseThrow(() -> new IllegalArgumentException("La tutoría no existe"));
        
        // Validar que el evaluador existe y cargar sus datos completos
        User evaluator = userRepository.findById(feedback.getEvaluator().getId())
            .orElseThrow(() -> new IllegalArgumentException("El evaluador no existe"));
        
        // Establecer la fecha actual
        feedback.setEvaluationDate(new Date());
        
        // Asignar los objetos completos
        feedback.setTutoring(tutoring);
        feedback.setEvaluator(evaluator);
        
        // Guardar el feedback
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback creado exitosamente con ID: {}", savedFeedback.getId());
        
        return savedFeedback;
    }
}