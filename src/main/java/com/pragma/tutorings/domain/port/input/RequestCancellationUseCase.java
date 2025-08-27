package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Tutoring;

public interface RequestCancellationUseCase {
    Tutoring requestCancellation(String tutoringId, String userId, String cancellationReason);
}