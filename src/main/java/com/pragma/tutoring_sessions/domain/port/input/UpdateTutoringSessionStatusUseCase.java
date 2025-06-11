package com.pragma.tutoring_sessions.domain.port.input;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;

public interface UpdateTutoringSessionStatusUseCase {
    TutoringSession updateSessionStatus(String sessionId, TutoringsSessionStatus newStatus, String notes);
}