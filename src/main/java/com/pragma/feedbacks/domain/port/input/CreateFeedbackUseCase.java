package com.pragma.feedbacks.domain.port.input;

import com.pragma.feedbacks.domain.model.Feedback;

public interface CreateFeedbackUseCase {
    Feedback createFeedback(Feedback feedback);
}