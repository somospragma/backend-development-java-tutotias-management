package com.pragma.feedbacks.infrastructure.adapter.output.persistence.mapper;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.infrastructure.adapter.output.persistence.entity.FeedbackEntity;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.TutoringEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.mapper.TutoringMapper;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    private final UserMapper userMapper;
    private final TutoringMapper tutoringMapper;

    public Feedback toDomain(FeedbackEntity entity) {
        if (entity == null) {
            return null;
        }

        Feedback feedback = new Feedback();
        feedback.setId(entity.getId());
        feedback.setEvaluator(userMapper.toDomain(entity.getEvaluatorId()));
        feedback.setEvaluationDate(entity.getEvaluationDate());
        feedback.setTutoring(tutoringMapper.toDomain(entity.getTutoringId()));
        feedback.setScore(entity.getScore());
        feedback.setComments(entity.getComments());

        return feedback;
    }

    public FeedbackEntity toEntity(Feedback domain) {
        if (domain == null) {
            return null;
        }

        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(domain.getId());
        
        User evaluator = domain.getEvaluator();
        if (evaluator != null) {
            UsersEntity evaluatorEntity = new UsersEntity();
            evaluatorEntity.setId(evaluator.getId());
            entity.setEvaluatorId(evaluatorEntity);
        }
        
        entity.setEvaluationDate(domain.getEvaluationDate());
        
        Tutoring tutoring = domain.getTutoring();
        if (tutoring != null) {
            TutoringEntity tutoringEntity = new TutoringEntity();
            tutoringEntity.setId(tutoring.getId());
            entity.setTutoringId(tutoringEntity);
        }
        
        entity.setScore(domain.getScore());
        entity.setComments(domain.getComments());

        return entity;
    }

    public List<Feedback> toDomainList(List<FeedbackEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}