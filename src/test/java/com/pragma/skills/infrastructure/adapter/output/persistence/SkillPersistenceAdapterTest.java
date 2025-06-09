package com.pragma.skills.infrastructure.adapter.output.persistence;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.infrastructure.adapter.output.persistence.entity.SkillEntity;
import com.pragma.skills.infrastructure.adapter.output.persistence.mapper.SkillMapper;
import com.pragma.skills.infrastructure.adapter.output.persistence.repository.SpringDataSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SkillPersistenceAdapterTest {

    @Mock
    private SpringDataSkillRepository repository;

    @Mock
    private SkillMapper mapper;

    @InjectMocks
    private SkillPersistenceAdapter adapter;

    private Skill testSkill;
    private SkillEntity testSkillEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testSkill = new Skill();
        testSkill.setId("1");
        testSkill.setName("Java");

        testSkillEntity = new SkillEntity();
        testSkillEntity.setId("1");
        testSkillEntity.setName("Java");
    }

    @Test
    void save_ShouldReturnSavedSkill() {
        // Arrange
        when(mapper.toEntity(testSkill)).thenReturn(testSkillEntity);
        when(repository.save(testSkillEntity)).thenReturn(testSkillEntity);
        when(mapper.toDomain(testSkillEntity)).thenReturn(testSkill);

        // Act
        Skill result = adapter.save(testSkill);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Java", result.getName());
        verify(mapper).toEntity(testSkill);
        verify(repository).save(testSkillEntity);
        verify(mapper).toDomain(testSkillEntity);
    }

    @Test
    void findById_WhenSkillExists_ShouldReturnSkill() {
        // Arrange
        when(repository.findById("1")).thenReturn(Optional.of(testSkillEntity));
        when(mapper.toDomain(testSkillEntity)).thenReturn(testSkill);

        // Act
        Optional<Skill> result = adapter.findById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("Java", result.get().getName());
        verify(repository).findById("1");
        verify(mapper).toDomain(testSkillEntity);
    }

    @Test
    void findById_WhenSkillDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Skill> result = adapter.findById("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById("nonexistent");
        verify(mapper, never()).toDomain(any(SkillEntity.class));
    }

    @Test
    void findAll_ShouldReturnAllSkills() {
        // Arrange
        SkillEntity skillEntity2 = new SkillEntity();
        skillEntity2.setId("2");
        skillEntity2.setName("Python");
        
        Skill skill2 = new Skill();
        skill2.setId("2");
        skill2.setName("Python");
        
        List<SkillEntity> entities = Arrays.asList(testSkillEntity, skillEntity2);
        
        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDomain(testSkillEntity)).thenReturn(testSkill);
        when(mapper.toDomain(skillEntity2)).thenReturn(skill2);

        // Act
        List<Skill> result = adapter.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Python", result.get(1).getName());
        verify(repository).findAll();
        verify(mapper, times(2)).toDomain(any(SkillEntity.class));
    }

    @Test
    void deleteById_WhenSkillExists_ShouldReturnTrue() {
        // Arrange
        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        // Act
        boolean result = adapter.deleteById("1");

        // Assert
        assertTrue(result);
        verify(repository).existsById("1");
        verify(repository).deleteById("1");
    }

    @Test
    void deleteById_WhenSkillDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(repository.existsById(anyString())).thenReturn(false);

        // Act
        boolean result = adapter.deleteById("nonexistent");

        // Assert
        assertFalse(result);
        verify(repository).existsById("nonexistent");
        verify(repository, never()).deleteById(anyString());
    }
}