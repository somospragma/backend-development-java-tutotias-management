package com.pragma.tutoring_sessions.domain.port.input;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;

import java.util.List;

public interface GetTutoringSessionsUseCase {
    List<TutoringSession> getSessionsByTutoringId(String tutoringId);
}