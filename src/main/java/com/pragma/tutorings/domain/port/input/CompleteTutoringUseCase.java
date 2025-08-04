package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Tutoring;

public interface CompleteTutoringUseCase {
    Tutoring completeTutoring(String tutoringId, String userId, String finalActUrl);
}