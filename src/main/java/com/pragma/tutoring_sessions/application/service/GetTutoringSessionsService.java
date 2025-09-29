package com.pragma.tutoring_sessions.application.service;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.input.GetTutoringSessionsUseCase;
import com.pragma.tutoring_sessions.domain.port.output.TutoringSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTutoringSessionsService implements GetTutoringSessionsUseCase {

    private final TutoringSessionRepository tutoringSessionRepository;

    @Override
    public List<TutoringSession> getSessionsByTutoringId(String tutoringId) {
        return tutoringSessionRepository.findByTutoringId(tutoringId);
    }
}