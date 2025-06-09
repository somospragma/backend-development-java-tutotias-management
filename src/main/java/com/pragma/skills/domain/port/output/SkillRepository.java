package com.pragma.skills.domain.port.output;

import com.pragma.skills.domain.model.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    Skill save(Skill skill);
    Optional<Skill> findById(String id);
    List<Skill> findAll();
    boolean deleteById(String id);
}