package com.pragma.feedbacks.domain.port.output;

import com.pragma.feedbacks.domain.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Feedback save(Feedback feedback);
    Optional<Feedback> findById(String id);
    List<Feedback> findByTutoringId(String tutoringId);
    List<Feedback> findByTutoringIdAndEvaluatorId(String tutoringId, String evaluatorId);
}