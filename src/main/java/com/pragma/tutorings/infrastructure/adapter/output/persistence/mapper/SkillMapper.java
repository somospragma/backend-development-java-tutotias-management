package com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper;

import com.pragma.tutorings.domain.model.Skill;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    Skill toDomain(SkillEntity entity);
    
    SkillEntity toEntity(Skill domain);
}