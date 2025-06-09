package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringRequestsEntity;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.mapper.TutoringRequestMapper;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.repository.SpringDataTutoringRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TutoringRequestPersistenceAdapter implements TutoringRequestRepository {

    private final SpringDataTutoringRequestRepository repository;
    private final TutoringRequestMapper mapper;

    @Override
    public TutoringRequest save(TutoringRequest tutoringRequest) {
        try {
            TutoringRequestsEntity entity = mapper.toEntity(tutoringRequest);
            entity.setAssignedTutoringId(null);
            TutoringRequestsEntity savedEntity = repository.saveAndFlush(entity);
            return mapper.toDomain(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la solicitud de tutoría: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<TutoringRequest> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<TutoringRequest> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}