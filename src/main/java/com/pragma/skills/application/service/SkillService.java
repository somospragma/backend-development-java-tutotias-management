package com.pragma.skills.application.service;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.domain.port.input.CreateSkillUseCase;
import com.pragma.skills.domain.port.input.DeleteSkillUseCase;
import com.pragma.skills.domain.port.input.FindSkillUseCase;
import com.pragma.skills.domain.port.input.UpdateSkillUseCase;
import com.pragma.skills.domain.port.output.SkillRepository;
import com.pragma.skills.domain.port.input.GetAllSkillsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService implements CreateSkillUseCase, FindSkillUseCase, GetAllSkillsUseCase, UpdateSkillUseCase, DeleteSkillUseCase {

    private final SkillRepository skillRepository;

    @Override
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Optional<Skill> findSkillById(String id) {
        return skillRepository.findById(id);
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<Skill> updateSkill(String id, Skill updatedSkill) {
        return skillRepository.findById(id)
                .map(existingSkill -> {
                    updatedSkill.setId(id);
                    return skillRepository.save(updatedSkill);
                });
    }

    @Override
    public boolean deleteSkill(String id) {
        return skillRepository.deleteById(id);
    }
}