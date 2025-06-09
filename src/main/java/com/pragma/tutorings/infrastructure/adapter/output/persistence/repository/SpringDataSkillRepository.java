package com.pragma.tutorings.infrastructure.adapter.output.persistence.repository;

import com.pragma.tutorings.infrastructure.adapter.output.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSkillRepository extends JpaRepository<SkillEntity, String> {
}