package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Tutoring;

public interface CancelTutoringUseCase {
    Tutoring cancelTutoring(String tutoringId, String adminId, String cancellationComment);
}