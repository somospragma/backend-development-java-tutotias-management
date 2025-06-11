package com.pragma.feedbacks.infrastructure.adapter.output.persistence.mapper;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.infrastructure.adapter.output.persistence.entity.FeedbackEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.TutoringMapper;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TutoringMapper.class})
public interface FeedbackMapper {
    
    @Mapping(source = "evaluator", target = "evaluatorId")
    @Mapping(source = "tutoring", target = "tutoringId")
    FeedbackEntity toEntity(Feedback feedback);
    
    @Mapping(source = "evaluatorId", target = "evaluator")
    @Mapping(source = "tutoringId", target = "tutoring")
    Feedback toDomain(FeedbackEntity entity);
    
    List<Feedback> toDomainList(List<FeedbackEntity> entities);
}