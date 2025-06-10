package com.pragma.tutorings.infrastructure.adapter.output.persistence.entity;

import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tutoring")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private UsersEntity tutorId; // "ID del usuario que actúa como tutor"

    @ManyToOne(fetch = FetchType.EAGER)
    private UsersEntity tuteeId; // "ID del usuario que actúa como tutorado"

    @ManyToMany
    private List<SkillEntity> skills; // "skills que se van a tutorar"

    @Column(name = "start_date")
    private Date start_date; // "Fecha de inicio de la tutoría"

    @Column(name = "expected_end_date")
    private Date expected_end_date; // "Fecha esperada de finalización"

    @Column(name = "status")
    @Enumerated(EnumType.STRING) // O EnumType.ORDINAL si quieres guardar el índice numérico
    private TutoringStatus status; // "Estado de la tutoría (Activa, Completada, Cancelada)"

    @Column(name = "objectives")
    private String objectives; // "Objetivos detallados de la tutoría"
}