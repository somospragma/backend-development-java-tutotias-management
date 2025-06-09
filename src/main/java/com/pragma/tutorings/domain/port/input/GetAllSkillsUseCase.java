package com.pragma.tutorings.domain.port.input;

import com.pragma.tutorings.domain.model.Skill;
import java.util.List;

public interface GetAllSkillsUseCase {
    List<Skill> getAllSkills();
}