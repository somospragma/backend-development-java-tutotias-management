package com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.repository;

import com.pragma.tutoring_sessions.infrastructure.adapter.output.persistence.entity.TutoringSessionsEntity;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.TutoringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTutoringSessionRepository extends JpaRepository<TutoringSessionsEntity, String> {
    List<TutoringSessionsEntity> findByTutoringId(TutoringEntity tutoringId);
}