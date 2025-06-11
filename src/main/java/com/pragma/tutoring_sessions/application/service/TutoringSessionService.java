package com.pragma.tutoring_sessions.application.service;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.input.CreateTutoringSessionUseCase;
import com.pragma.tutoring_sessions.domain.port.input.UpdateTutoringSessionStatusUseCase;
import com.pragma.tutoring_sessions.domain.port.output.TutoringSessionRepository;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TutoringSessionService implements CreateTutoringSessionUseCase, UpdateTutoringSessionStatusUseCase {

    private final TutoringSessionRepository tutoringSessionRepository;
    private final TutoringRepository tutoringRepository;

    @Override
    public TutoringSession createTutoringSession(String tutoringId, String datetime, int durationMinutes, String locationLink, String topicsCovered) {
        // Validate tutoring exists
        Tutoring tutoring = tutoringRepository.findById(tutoringId)
                .orElseThrow(() -> new IllegalArgumentException("Tutoring not found with id: " + tutoringId));
        
        // Create new tutoring session
        TutoringSession tutoringSession = new TutoringSession();
        tutoringSession.setTutoring(tutoring);
        tutoringSession.setDatetime(datetime);
        tutoringSession.setDurationMinutes(durationMinutes);
        tutoringSession.setLocationLink(locationLink);
        tutoringSession.setTopicsCovered(topicsCovered);
        tutoringSession.setSessionStatus(TutoringsSessionStatus.Programada); // Default status
        
        return tutoringSessionRepository.save(tutoringSession);
    }

    @Override
    public TutoringSession updateSessionStatus(String sessionId, TutoringsSessionStatus newStatus, String notes) {
        // Validate session exists
        tutoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Tutoring session not found with id: " + sessionId));
        
        // Update status and notes
        return tutoringSessionRepository.updateStatus(sessionId, newStatus, notes);
    }
}