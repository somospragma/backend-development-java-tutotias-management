package com.pragma.tutorings_requests.domain.port.input;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;

public interface CreateTutoringRequestUseCase {
    TutoringRequest createTutoringRequest(TutoringRequest tutoringRequest);
}