package com.pragma.skills.domain.port.input;

import com.pragma.skills.domain.model.Skill;

import java.util.List;

public interface GetAllSkillsUseCase {
    List<Skill> getAllSkills();
}