package com.pragma.skills.domain.port.input;

import com.pragma.skills.domain.model.Skill;

public interface CreateSkillUseCase {
    Skill createSkill(Skill skill);
}