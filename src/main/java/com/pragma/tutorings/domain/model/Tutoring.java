package com.pragma.tutorings.domain.model;

import com.pragma.skills.domain.model.Skill;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.usuarios.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tutoring {
    private String id;
    private User tutor;
    private User tutee;
    private List<Skill> skills;
    private Date startDate;
    private Date expectedEndDate;
    private TutoringStatus status;
    private String objectives;
}