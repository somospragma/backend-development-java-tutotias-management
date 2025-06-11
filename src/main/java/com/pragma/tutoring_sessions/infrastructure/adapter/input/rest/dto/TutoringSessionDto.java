package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto;

import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringSessionDto {
    private String id;
    private String datetime;
    private int durationMinutes;
    private String locationLink;
    private String topicsCovered;
    private String notes;
    private TutoringsSessionStatus sessionStatus;
}