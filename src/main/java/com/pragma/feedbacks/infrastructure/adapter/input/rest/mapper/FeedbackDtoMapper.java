package com.pragma.feedbacks.infrastructure.adapter.input.rest.mapper;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.dto.CreateFeedbackDto;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.dto.FeedbackDto;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.TutoringDtoMapper;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, TutoringDtoMapper.class})
public interface FeedbackDtoMapper {
    
    FeedbackDto toDto(Feedback feedback);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evaluationDate", ignore = true)
    @Mapping(source = "evaluatorId", target = "evaluator.id")
    @Mapping(source = "tutoringId", target = "tutoring.id")
    Feedback toDomain(CreateFeedbackDto dto);
}