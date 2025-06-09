package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.mapper;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.skills.infrastructure.adapter.output.persistence.mapper.SkillMapper;
import com.pragma.skills.infrastructure.adapter.output.persistence.repository.SpringDataSkillRepository;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringRequestsEntity;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.mapper.UserMapper;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.repository.SpringDataUserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring", 
    uses = {SkillMapper.class, UserMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TutoringRequestMapper {

    @Autowired
    protected SpringDataSkillRepository skillRepository;
    
    @Autowired
    protected SpringDataUserRepository userRepository;

    @Mapping(target = "assignedTutoringId", ignore = true)
    @Mapping(target = "skills", expression = "java(mapSkills(tutoringRequest))")
    @Mapping(target = "tutee", expression = "java(mapUser(tutoringRequest))")
    public abstract TutoringRequestsEntity toEntity(TutoringRequest tutoringRequest);

    @Mapping(target = "skills", source = "skills")
    @Mapping(target = "tutee", source = "tutee")
    public abstract TutoringRequest toDomain(TutoringRequestsEntity entity);
    
    protected List<SkillEntity> mapSkills(TutoringRequest tutoringRequest) {
        if (tutoringRequest.getSkills() == null || tutoringRequest.getSkills().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> skillIds = tutoringRequest.getSkills().stream()
                .map(Skill::getId)
                .collect(Collectors.toList());
                
        return skillRepository.findAllById(skillIds);
    }
    
    protected UsersEntity mapUser(TutoringRequest tutoringRequest) {
        if (tutoringRequest.getTutee() == null || tutoringRequest.getTutee().getId() == null) {
            return null;
        }
        
        return userRepository.findById(tutoringRequest.getTutee().getId()).orElse(null);
    }
}