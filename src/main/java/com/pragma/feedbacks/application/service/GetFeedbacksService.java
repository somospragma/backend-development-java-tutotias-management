package com.pragma.feedbacks.application.service;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.domain.port.input.GetFeedbacksUseCase;
import com.pragma.feedbacks.domain.port.output.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFeedbacksService implements GetFeedbacksUseCase {

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getFeedbacksByTutoringId(String tutoringId) {
        return feedbackRepository.findByTutoringId(tutoringId);
    }
}