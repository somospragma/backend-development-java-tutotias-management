package com.pragma.tutorings.infrastructure.adapter.input.rest;

import com.pragma.tutorings.domain.model.Skill;
import com.pragma.tutorings.domain.port.input.CreateSkillUseCase;
import com.pragma.tutorings.domain.port.input.DeleteSkillUseCase;
import com.pragma.tutorings.domain.port.input.FindSkillUseCase;
import com.pragma.tutorings.domain.port.input.GetAllSkillsUseCase;
import com.pragma.tutorings.domain.port.input.UpdateSkillUseCase;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.CreateSkillDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.SkillDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.UpdateSkillDto;
import com.pragma.tutorings.infrastructure.adapter.input.rest.mapper.SkillDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final CreateSkillUseCase createSkillUseCase;
    private final FindSkillUseCase findSkillUseCase;
    private final GetAllSkillsUseCase getAllSkillsUseCase;
    private final UpdateSkillUseCase updateSkillUseCase;
    private final DeleteSkillUseCase deleteSkillUseCase;
    private final SkillDtoMapper skillDtoMapper;

    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody CreateSkillDto createSkillDto) {
        Skill skill = skillDtoMapper.toModel(createSkillDto);
        skill.setId(UUID.randomUUID().toString());
        Skill createdSkill = createSkillUseCase.createSkill(skill);
        return new ResponseEntity<>(skillDtoMapper.toDto(createdSkill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable String id) {
        return findSkillUseCase.findSkillById(id)
                .map(skill -> ResponseEntity.ok(skillDtoMapper.toDto(skill)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        List<SkillDto> skills = getAllSkillsUseCase.getAllSkills().stream()
                .map(skillDtoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skills);
    }

    @PutMapping
    public ResponseEntity<SkillDto> updateSkill(@Valid @RequestBody UpdateSkillDto updateSkillDto) {
        Skill skill = skillDtoMapper.toModel(updateSkillDto);
        return updateSkillUseCase.updateSkill(updateSkillDto.getId(), skill)
                .map(updatedSkill -> ResponseEntity.ok(skillDtoMapper.toDto(updatedSkill)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable String id) {
        boolean deleted = deleteSkillUseCase.deleteSkill(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}