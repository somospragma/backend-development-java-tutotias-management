package com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto;

import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringRequestFilterDto {
    private String tuteeId;
    private String skillId;
    private RequestStatus status;
}