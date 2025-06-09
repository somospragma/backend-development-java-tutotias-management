package com.pragma.tutorings.infrastructure.adapter.output.persistence;

import com.pragma.tutorings.domain.model.Skill;
import com.pragma.tutorings.domain.port.output.SkillRepository;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.SkillMapper;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.repository.SpringDataSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SkillPersistenceAdapter implements SkillRepository {

    private final SpringDataSkillRepository repository;
    private final SkillMapper mapper;

    @Override
    public Skill save(Skill skill) {
        SkillEntity entity = mapper.toEntity(skill);
        SkillEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Skill> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}