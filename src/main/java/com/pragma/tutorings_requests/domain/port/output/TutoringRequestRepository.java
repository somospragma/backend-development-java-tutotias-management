package com.pragma.tutorings_requests.domain.port.output;

import com.pragma.tutorings_requests.domain.model.TutoringRequest;

import java.util.List;
import java.util.Optional;

public interface TutoringRequestRepository {
    TutoringRequest save(TutoringRequest tutoringRequest);
    Optional<TutoringRequest> findById(String id);
    List<TutoringRequest> findAll();
}