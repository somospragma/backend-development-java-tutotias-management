package com.pragma.tutorings_requests.domain.port.input;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;

import java.util.List;

public interface GetTutoringRequestsUseCase {
    List<TutoringRequest> getAllTutoringRequests();
    List<TutoringRequest> getTutoringRequestsWithFilters(String tuteeId, String skillId, RequestStatus status);
}