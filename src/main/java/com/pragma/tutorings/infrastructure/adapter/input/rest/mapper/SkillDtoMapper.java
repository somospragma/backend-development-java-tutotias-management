package com.pragma.tutorings.infrastructure.adapter.input.rest.mapper;

import com.pragma.tutorings.domain.model.Skill;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CreateSkillDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.SkillDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.UpdateSkillDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SkillDtoMapper {
    @Mapping(target = "id", ignore = true)
    Skill toModel(CreateSkillDto dto);
    
    Skill toModel(UpdateSkillDto dto);
    
    SkillDto toDto(Skill skill);
}