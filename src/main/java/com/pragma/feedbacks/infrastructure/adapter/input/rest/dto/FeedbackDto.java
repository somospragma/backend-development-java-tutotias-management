package com.pragma.feedbacks.infrastructure.adapter.input.rest.dto;

import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private String id;
    private UserDto evaluator;
    private Date evaluationDate;
    private TutoringDto tutoring;
    private String score;
    private String comments;
}