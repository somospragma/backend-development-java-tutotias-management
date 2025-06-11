package com.pragma.tutoring_sessions.domain.port.input;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;

public interface CreateTutoringSessionUseCase {
    TutoringSession createTutoringSession(String tutoringId, String datetime, int durationMinutes, String locationLink, String topicsCovered);
}