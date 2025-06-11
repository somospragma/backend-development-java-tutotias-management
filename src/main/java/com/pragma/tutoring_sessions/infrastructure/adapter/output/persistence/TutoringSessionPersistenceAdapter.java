package com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.domain.port.output.TutoringSessionRepository;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.entity.TutoringSessionsEntity;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.mapper.TutoringSessionMapper;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.repository.SpringDataTutoringSessionRepository;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.TutoringEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.TutoringMapper;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TutoringSessionPersistenceAdapter implements TutoringSessionRepository {

    private final SpringDataTutoringSessionRepository repository;
    private final TutoringSessionMapper mapper;

    @Override
    public TutoringSession save(TutoringSession tutoringSession) {
        TutoringSessionsEntity entity = mapper.toEntity(tutoringSession);
        TutoringSessionsEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<TutoringSession> findById(String id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<TutoringSession> findByTutoringId(String tutoringId) {
        TutoringEntity tutoringEntity = new TutoringEntity();
        tutoringEntity.setId(tutoringId);
        
        return repository.findByTutoringId(tutoringEntity)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public TutoringSession updateStatus(String id, TutoringsSessionStatus newStatus, String notes) {
        TutoringSessionsEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tutoring session not found with id: " + id));
        
        entity.setSessionStatus(newStatus);
        
        // Update notes if provided
        if (notes != null && !notes.isEmpty()) {
            entity.setNotes(notes);
        }
        
        TutoringSessionsEntity updatedEntity = repository.save(entity);
        
        return mapper.toModel(updatedEntity);
    }
}