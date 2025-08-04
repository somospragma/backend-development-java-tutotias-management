package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.repository;

import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringRequestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTutoringRequestRepository extends JpaRepository<TutoringRequestsEntity, String> {
    @Query("SELECT DISTINCT tr FROM TutoringRequestsEntity tr " +
           "LEFT JOIN tr.skills s " +
           "WHERE (:tuteeId IS NULL OR tr.tutee.id = :tuteeId) " +
           "AND (:skillId IS NULL OR s.id = :skillId) " +
           "AND (:status IS NULL OR tr.requestStatus = :status) " +
           "AND (:chapterId IS NULL OR tr.tutee.chapter.id = :chapterId)")
    List<TutoringRequestsEntity> findWithFilters(
            @Param("tuteeId") String tuteeId,
            @Param("skillId") String skillId,
            @Param("status") RequestStatus status,
            @Param("chapterId") String chapterId);
            
    @Modifying
    @Query(value = "UPDATE tutoring_requests SET assigned_tutoring_id = :tutoringId WHERE id = :requestId", nativeQuery = true)
    void updateAssignedTutoringId(@Param("requestId") String requestId, @Param("tutoringId") String tutoringId);
}