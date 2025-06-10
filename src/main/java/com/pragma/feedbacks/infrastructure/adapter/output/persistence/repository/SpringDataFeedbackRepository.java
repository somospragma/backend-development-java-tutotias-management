package com.pragma.feedbacks.infrastructure.adapter.output.persistence.repository;

import com.pragma.feedbacks.infrastructure.adapter.output.persistence.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataFeedbackRepository extends JpaRepository<FeedbackEntity, String> {
    List<FeedbackEntity> findByTutoringIdIdAndEvaluatorIdId(String tutoringId, String evaluatorId);
    List<FeedbackEntity> findByTutoringIdId(String tutoringId);
}