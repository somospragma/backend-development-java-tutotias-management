package com.pragma.tutorings.infrastructure.adapter.output.persistence.entity;

import com.pragma.tutorings.domain.model.enums.RequestStatus;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tutoring_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringRequestsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private UsersEntity tuteeId; // "ID del usuario que realiza la solicitud"

    @ManyToMany
    private List<SkillEntity> skills; // "habilidades solicitada"

    @Column(name = "needs_description", nullable = false)
    private String needsDescription; // "Descripción de la necesidad de tutoría"

    @Column(name = "request_date", nullable = false)
    private Date requestDate; // "Fecha en que se realizó la solicitud"

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING) // O EnumType.ORDINAL si quieres guardar el índice numérico
    private RequestStatus requestStatus; // "Estado de la solicitud (Enviada(default), Aprobada, Asignada, Rechazada)"

    @Column(name = "assigned_tutoring_id")
    private TutoringEntity assignedTutoringId; // "ID de la tutoría resultante de esta solicitud (puede ser NULL)"
}