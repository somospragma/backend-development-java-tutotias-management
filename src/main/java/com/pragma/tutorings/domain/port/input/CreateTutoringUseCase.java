package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Tutoring;

public interface CreateTutoringUseCase {
    Tutoring createTutoring(String tutoringRequestId, String tutorId, String objectives);
}