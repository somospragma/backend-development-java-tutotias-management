package com.pragma.tutorings_requests.domain.port.input;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;

public interface UpdateTutoringRequestStatusUseCase {
    TutoringRequest updateStatus(String requestId, RequestStatus newStatus);
}