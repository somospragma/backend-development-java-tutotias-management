package com.pragma.skills.infrastructure.adapter.output.persistence.repository;

import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataSkillRepository extends JpaRepository<SkillEntity, String> {
    @Query("SELECT s FROM SkillEntity s WHERE s.id IN :ids")
    List<SkillEntity> findAllByIdIn(@Param("ids") List<String> ids);
}