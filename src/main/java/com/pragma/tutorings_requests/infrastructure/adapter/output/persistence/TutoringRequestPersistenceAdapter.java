package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence;

import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.skills.infrastructure.adapter.output.persistence.mapper.SkillMapper;
import com.pragma.skills.infrastructure.adapter.output.persistence.repository.SpringDataSkillRepository;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringRequestsEntity;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.mapper.TutoringRequestMapper;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.repository.SpringDataTutoringRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TutoringRequestPersistenceAdapter implements TutoringRequestRepository {

    private final SpringDataTutoringRequestRepository repository;
    private final SpringDataSkillRepository skillRepository;
    private final TutoringRequestMapper mapper;
    private final SkillMapper skillMapper;

    @Transactional
    @Override
    public TutoringRequest save(TutoringRequest tutoringRequest) {
        try {
            TutoringRequestsEntity entity = mapper.toEntity(tutoringRequest);
            
            // Manejar la relación ManyToMany con Skills
            if (entity.getSkills() != null && !entity.getSkills().isEmpty()) {
                List<SkillEntity> managedSkills = new ArrayList<>();

                for (SkillEntity skill : entity.getSkills()) {
                    // Buscar la habilidad existente por ID
                    Optional<SkillEntity> existingSkill = skillRepository.findById(skill.getId());
                    // Usar la entidad gestionada desde la base de datos
                    existingSkill.ifPresent(managedSkills::add);
                }

                // Reemplazar la lista de habilidades con las entidades gestionadas
                entity.setSkills(managedSkills);
            }
            
            TutoringRequestsEntity savedEntity = repository.save(entity);
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
    
    @Override
    public List<TutoringRequest> findWithFilters(String tuteeId, String skillId, RequestStatus status) {
        return repository.findWithFilters(tuteeId, skillId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}