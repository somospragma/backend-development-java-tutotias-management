package com.pragma.feedbacks.infrastructure.adapter.output.persistence;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.output.FeedbackRepository;
import com.pragma.feedbacks.infrastructure.adapter.output.persistence.entity.FeedbackEntity;
import com.pragma.feedbacks.infrastructure.adapter.output.persistence.mapper.FeedbackMapper;
import com.pragma.feedbacks.infrastructure.adapter.output.persistence.repository.SpringDataFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeedbackPersistenceAdapter implements FeedbackRepository {

    private final SpringDataFeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Feedback save(Feedback feedback) {
        FeedbackEntity entity = feedbackMapper.toEntity(feedback);
        FeedbackEntity savedEntity = feedbackRepository.save(entity);
        return feedbackMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Feedback> findById(String id) {
        return feedbackRepository.findById(id)
                .map(feedbackMapper::toDomain);
    }

    @Override
    public List<Feedback> findByTutoringId(String tutoringId) {
        List<FeedbackEntity> entities = feedbackRepository.findByTutoringIdId(tutoringId);
        return feedbackMapper.toDomainList(entities);
    }

    @Override
    public List<Feedback> findByTutoringIdAndEvaluatorId(String tutoringId, String evaluatorId) {
        List<FeedbackEntity> entities = feedbackRepository.findByTutoringIdIdAndEvaluatorIdId(tutoringId, evaluatorId);
        return feedbackMapper.toDomainList(entities);
    }
}