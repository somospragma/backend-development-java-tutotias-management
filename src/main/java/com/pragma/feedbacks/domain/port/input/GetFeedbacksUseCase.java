package com.pragma.feedbacks.domain.port.input;

import com.pragma.feedbacks.domain.model.Feedback;

import java.util.List;

public interface GetFeedbacksUseCase {
    List<Feedback> getFeedbacksByTutoringId(String tutoringId);
}