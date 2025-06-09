package com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto;

import com.pragma.skills.infrastructure.adapter.input.rest.dto.SkillDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringRequestDto {
    private String id;
    private UserDto tutee;
    // Lista de habilidades opcional
    private List<SkillDto> skill;
    private String needsDescription;
    private String requestStatus;
}