package com.pragma.skills.infrastructure.adapter.output.persistence.mapper;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    Skill toDomain(SkillEntity entity);
    
    SkillEntity toEntity(Skill domain);
}