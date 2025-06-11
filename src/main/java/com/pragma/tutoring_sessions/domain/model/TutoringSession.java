package com.pragma.tutoring_sessions.domain.model;

import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringSession {
    private String id;
    private Tutoring tutoring;
    private String datetime;
    private int durationMinutes;
    private String locationLink;
    private String topicsCovered;
    private String notes;
    private TutoringsSessionStatus sessionStatus;
}