package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Tutoring;

import java.util.List;

public interface GetTutoringsUseCase {
    List<Tutoring> getAllTutorings();
    List<Tutoring> getTutoringsByTutorId(String tutorId);
    List<Tutoring> getTutoringsByTuteeId(String tuteeId);
    Tutoring getTutoringById(String id);
}