package com.pragma.tutorings.infrastructure.adapter.input.rest.dto;

import com.pragma.skills.infrastructure.adapter.input.rest.dto.SkillDto;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringDto {
    private String id;
    private UserDto tutor;
    private UserDto tutee;
    private List<SkillDto> skills;
    private Date startDate;
    private Date expectedEndDate;
    private TutoringStatus status;
    private String objectives;
    private String finalActUrl;
    private Date createdAt;
    private Date updatedAt;
}