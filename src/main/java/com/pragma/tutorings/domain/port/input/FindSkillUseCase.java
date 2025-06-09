package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Skill;
import java.util.Optional;

public interface FindSkillUseCase {
    Optional<Skill> findSkillById(String id);
}