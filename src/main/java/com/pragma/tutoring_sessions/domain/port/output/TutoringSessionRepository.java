package com.pragma.tutoring_sessions.domain.port.output;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;

import java.util.List;
import java.util.Optional;

public interface TutoringSessionRepository {
    TutoringSession save(TutoringSession tutoringSession);
    Optional<TutoringSession> findById(String id);
    List<TutoringSession> findByTutoringId(String tutoringId);
    TutoringSession updateStatus(String id, TutoringsSessionStatus newStatus, String notes);
}