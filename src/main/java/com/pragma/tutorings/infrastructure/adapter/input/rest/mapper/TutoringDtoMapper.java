package com.pragma.tutorings.infrastructure.adapter.input.rest.mapper;

import com.pragma.skills.infrastructure.adapter.input.rest.mapper.SkillDtoMapper;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, SkillDtoMapper.class})
public interface TutoringDtoMapper {
    TutoringDtoMapper INSTANCE = Mappers.getMapper(TutoringDtoMapper.class);

    TutoringDto toDto(Tutoring tutoring);
    List<TutoringDto> toDtoList(List<Tutoring> tutorings);
}