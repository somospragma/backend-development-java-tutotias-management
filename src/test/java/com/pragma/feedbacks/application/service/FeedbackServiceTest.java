package com.pragma.feedbacks.application.service;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.output.FeedbackRepository;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private TutoringRepository tutoringRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;
    private User evaluator;
    private Tutoring tutoring;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        evaluator = new User();
        evaluator.setId("evaluator-id");
        evaluator.setFirstName("Evaluator");
        evaluator.setLastName("Test");

        tutoring = new Tutoring();
        tutoring.setId("tutoring-id");

        feedback = new Feedback();
        feedback.setEvaluator(evaluator);
        feedback.setTutoring(tutoring);
        feedback.setScore("5");
        feedback.setComments("Excelente tutoría");
    }

    @Test
    void createFeedback_Success() {
        // Arrange
        User completeEvaluator = new User();
        completeEvaluator.setId("evaluator-id");
        completeEvaluator.setFirstName("Evaluator");
        completeEvaluator.setLastName("Test");
        
        Tutoring completeTutoring = new Tutoring();
        completeTutoring.setId("tutoring-id");
        
        when(tutoringRepository.findById("tutoring-id")).thenReturn(Optional.of(completeTutoring));
        when(userRepository.findById("evaluator-id")).thenReturn(Optional.of(completeEvaluator));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback savedFeedback = invocation.getArgument(0);
            savedFeedback.setId("feedback-id");
            return savedFeedback;
        });

        // Act
        Feedback result = feedbackService.createFeedback(feedback);

        // Assert
        assertNotNull(result);
        assertEquals("feedback-id", result.getId());
        assertEquals(completeEvaluator, result.getEvaluator());
        assertEquals(completeTutoring, result.getTutoring());
        assertEquals("5", result.getScore());
        assertEquals("Excelente tutoría", result.getComments());
        assertNotNull(result.getEvaluationDate());
        
        verify(tutoringRepository).findById("tutoring-id");
        verify(userRepository).findById("evaluator-id");
        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    void createFeedback_TutoringNotFound() {
        // Arrange
        when(tutoringRepository.findById("tutoring-id")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.createFeedback(feedback);
        });

        assertEquals("La tutoría no existe", exception.getMessage());
        verify(tutoringRepository).findById("tutoring-id");
        verify(userRepository, never()).findById(anyString());
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void createFeedback_EvaluatorNotFound() {
        // Arrange
        when(tutoringRepository.findById("tutoring-id")).thenReturn(Optional.of(tutoring));
        when(userRepository.findById("evaluator-id")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.createFeedback(feedback);
        });

        assertEquals("El evaluador no existe", exception.getMessage());
        verify(tutoringRepository).findById("tutoring-id");
        verify(userRepository).findById("evaluator-id");
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }
}