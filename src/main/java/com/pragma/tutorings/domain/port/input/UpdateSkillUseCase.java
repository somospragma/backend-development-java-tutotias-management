package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Skill;
import java.util.Optional;

public interface UpdateSkillUseCase {
    Optional<Skill> updateSkill(String id, Skill skill);
}