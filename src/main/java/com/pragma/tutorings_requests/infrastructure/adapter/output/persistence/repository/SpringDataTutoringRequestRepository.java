package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.repository;

import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringRequestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTutoringRequestRepository extends JpaRepository<TutoringRequestsEntity, String> {
}