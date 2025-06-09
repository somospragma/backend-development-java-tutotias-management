package com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto;

import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutoringRequestStatusDto {
    @NotNull(message = "El estado no puede ser nulo")
    private RequestStatus status;
}