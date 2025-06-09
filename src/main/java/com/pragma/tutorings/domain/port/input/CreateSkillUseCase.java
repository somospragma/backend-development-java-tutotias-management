package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Skill;

public interface CreateSkillUseCase {
    Skill createSkill(Skill skill);
}