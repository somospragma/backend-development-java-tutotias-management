package com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutoringSessionDto {
    @NotBlank(message = "Tutoring ID is required")
    private String tutoringId;
    
    @NotBlank(message = "Datetime is required")
    private String datetime;
    
    @NotNull(message = "Duration minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
    
    private String locationLink;
    
    private String topicsCovered;
}