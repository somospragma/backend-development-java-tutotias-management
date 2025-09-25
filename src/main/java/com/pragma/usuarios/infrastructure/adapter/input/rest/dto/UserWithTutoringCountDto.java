package com.pragma.usuarios.infrastructure.adapter.input.rest.dto;

import com.pragma.chapter.infrastructure.adapter.input.rest.dto.ChapterDto;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithTutoringCountDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String slackId;
    private ChapterDto chapter;
    private RolUsuario rol;
    private int activeTutoringLimit;
    private int seniority;
    private long tutoringsAsTutor;
    private long tutoringsAsTutee;
}