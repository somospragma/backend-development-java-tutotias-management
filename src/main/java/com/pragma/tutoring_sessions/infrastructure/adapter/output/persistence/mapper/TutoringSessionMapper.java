package com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.mapper;

import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.entity.TutoringSessionsEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.TutoringMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TutoringMapper.class})
public interface TutoringSessionMapper {

    @Mapping(source = "tutoringId", target = "tutoring")
    TutoringSession toModel(TutoringSessionsEntity entity);

    @Mapping(source = "tutoring", target = "tutoringId")
    TutoringSessionsEntity toEntity(TutoringSession model);
}