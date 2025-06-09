package com.pragma.tutorings_requests.infrastructure.adapter.input.rest.mapper;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.domain.port.input.FindSkillUseCase;
import com.pragma.skills.infrastructure.adapter.input.rest.mapper.SkillDtoMapper;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.CreateTutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.TutoringRequestDto;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.port.input.FindUserByIdUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {SkillDtoMapper.class, UserDtoMapper.class})
public abstract class  TutoringRequestDtoMapper {

    @Autowired
    private FindUserByIdUseCase findUserByIdUseCase;
    @Autowired
    private FindSkillUseCase findSkillUseCase;
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tutee", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "requestDate", ignore = true)
    @Mapping(target = "requestStatus", ignore = true)
    public abstract TutoringRequest toModel(CreateTutoringRequestDto dto);
    
    @Mapping(target = "skills", source = "skills")
    public abstract TutoringRequestDto toDto(TutoringRequest tutoringRequest);

    @AfterMapping
    protected void findAndSetTutte(CreateTutoringRequestDto dto, @MappingTarget TutoringRequest tutoringRequest) {
        if (dto.getTuteeId() != null && !dto.getTuteeId().isEmpty()) {
            User user = new User();
            user.setId(dto.getTuteeId());
            tutoringRequest.setTutee(user);
        }
    }

    @AfterMapping
    protected void findAndSetSkills(CreateTutoringRequestDto dto, @MappingTarget TutoringRequest tutoringRequest) {
        if (dto.getSkillIds() != null && !dto.getSkillIds().isEmpty()) {
            List<Skill> skills = dto.getSkillIds().stream()
                .map(id -> {
                    Skill skill = new Skill();
                    skill.setId(id);
                    return skill;
                })
                .toList();
            tutoringRequest.setSkills(skills);
        }
    }
    
    @AfterMapping
    protected void ensureSkillsNotNull(@MappingTarget TutoringRequestDto dto) {
        if (dto.getSkills() == null) {
            dto.setSkills(new ArrayList<>());
        }
    }
}