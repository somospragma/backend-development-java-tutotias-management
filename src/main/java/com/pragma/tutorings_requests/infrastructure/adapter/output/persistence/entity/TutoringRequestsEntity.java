package com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity;

import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tutoring_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoringRequestsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutee_id")
    private UsersEntity tutee; // "ID del usuario que realiza la solicitud"

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "tutoring_request_skills",
        joinColumns = @JoinColumn(name = "tutoring_request_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<SkillEntity> skills = new ArrayList<>(); // "habilidades solicitada"

    @Column(name = "needs_description", nullable = false)
    private String needsDescription; // "Descripción de la necesidad de tutoría"

    @Column(name = "request_date", nullable = false)
    private Date requestDate; // "Fecha en que se realizó la solicitud"

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus; // "Estado de la solicitud (Enviada(default), Aprobada, Asignada, Rechazada)"

    @OneToOne
    @JoinColumn(name ="assigned_tutoring_id",
            referencedColumnName ="id")
    private TutoringEntity assignedTutoringId; // "ID de la tutoría resultante de esta solicitud (puede ser NULL)"
}