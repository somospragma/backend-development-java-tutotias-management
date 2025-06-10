package com.pragma.feedbacks.domain.model;

import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.usuarios.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    private String id;
    private User evaluator;
    private Date evaluationDate;
    private Tutoring tutoring;
    private String score;
    private String comments;
}