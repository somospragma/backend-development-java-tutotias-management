package com.pragma.tutorings.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
    private String id;
    private String name;
    
    @ManyToMany(mappedBy = "skills")
    private List<TutoringEntity> tutorings;
}