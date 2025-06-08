package com.pragma.tutorings.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String Name; //"Nombre único de la habilidad o conocimiento"

    @ManyToMany
    private List<TutoringEntity> Tutoring; // "listado de tutorial asignadas al skills"
}