package com.pragma.skills.domain.port.input;

import com.pragma.skills.domain.model.Skill;

import java.util.Optional;

public interface FindSkillUseCase {
    Optional<Skill> findSkillById(String id);
}