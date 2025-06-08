package com.pragma.usuarios.infrastructure.adapter.output.persistence.entity;

import com.pragma.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name")
    private String firstName; // "Nombre del usuario"

    @Column(name = "last_name")
    private String lastName; //Apellido del usuario"

    @Column(name = "correo", unique = true, nullable = false)
    private String email; //"Correo electrónico único del usuario pragma"

    @ManyToOne
    private ChapterEntity chapter; // "chapter principal"

    @Column(name = "rol")
    @Enumerated(EnumType.STRING) // O EnumType.ORDINAL si quieres guardar el índice numérico
    private RolUsuario rol; // Rol del usuario (Tutor, Tutorado(default), Administrador, Gerente)"

    @Column(name = "active_tutoring_limit")
    private int activeTutoringLimit; // Número máximo de tutorías activas para un tutor"
}