package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.mapper;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.CreateTutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.TutoringSessionDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TutoringDtoMapper.class})
public interface TutoringSessionDtoMapper {

    @Mapping(target = "tutoring", ignore = true)
    TutoringSessionDto toDto(TutoringSession model);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tutoring", ignore = true)
    @Mapping(target = "topicsCovered", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "sessionStatus", ignore = true)
    TutoringSession toModel(CreateTutoringSessionDto dto);
}