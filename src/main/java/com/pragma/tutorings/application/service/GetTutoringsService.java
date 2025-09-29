package com.pragma.tutorings.application.service;

import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.port.input.GetTutoringsUseCase;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTutoringsService implements GetTutoringsUseCase {

    private final TutoringRepository tutoringRepository;

    @Override
    public List<Tutoring> getTutoringsByTutorId(String tutorId) {
        return tutoringRepository.findByTutorId(tutorId);
    }

    @Override
    public List<Tutoring> getTutoringsByTuteeId(String tuteeId) {
        return tutoringRepository.findByTuteeId(tuteeId);
    }

    @Override
    public List<Tutoring> getAllTutorings() {
        return tutoringRepository.findAll();
    }

    @Override
    public Tutoring getTutoringById(String id) {
        return tutoringRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutoring not found with id: " + id));
    }
}