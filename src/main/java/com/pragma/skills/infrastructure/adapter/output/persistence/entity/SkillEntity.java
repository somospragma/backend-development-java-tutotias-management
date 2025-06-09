package com.pragma.skills.infrastructure.adapter.output.persistence.entity;

import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.entity.TutoringEntity;
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
public class SkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    
    @ManyToMany(mappedBy = "skills")
    private List<TutoringEntity> tutorings;
}