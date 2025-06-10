package com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper;

import com.pragma.skills.infrastructure.adapter.output.persistence.mapper.SkillMapper;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.TutoringEntity;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillMapper.class})
public interface TutoringMapper {
    TutoringMapper INSTANCE = Mappers.getMapper(TutoringMapper.class);

    @Mapping(source = "tutor", target = "tutorId")
    @Mapping(source = "tutee", target = "tuteeId")
    @Mapping(source = "startDate", target = "start_date")
    @Mapping(source = "expectedEndDate", target = "expected_end_date")
    TutoringEntity toEntity(Tutoring tutoring);

    @Mapping(source = "tutorId", target = "tutor")
    @Mapping(source = "tuteeId", target = "tutee")
    @Mapping(source = "start_date", target = "startDate")
    @Mapping(source = "expected_end_date", target = "expectedEndDate")
    Tutoring toDomain(TutoringEntity entity);

    List<Tutoring> toDomainList(List<TutoringEntity> entities);
}