package com.pragma.tutorings.domain.port.output;

import com.pragma.tutorings.domain.model.Skill;
import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    Skill save(Skill skill);
    Optional<Skill> findById(String id);
    List<Skill> findAll();
    boolean deleteById(String id);
}