package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto;

import com.pragma.tutorings_requests.domain.model.enums.TutoringsSessionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutoringSessionStatusDto {
    @NotNull(message = "Status is required")
    private TutoringsSessionStatus status;
    
    private String notes;
}