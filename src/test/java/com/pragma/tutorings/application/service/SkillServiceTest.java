package com.pragma.tutorings.application.service;

import com.pragma.tutorings.domain.model.Skill;
import com.pragma.tutorings.domain.port.output.SkillRepository;
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

class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill testSkill;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testSkill = new Skill();
        testSkill.setId("1");
        testSkill.setName("Java");
    }

    @Test
    void createSkill_ShouldReturnCreatedSkill() {
        // Arrange
        when(skillRepository.save(any(Skill.class))).thenReturn(testSkill);

        // Act
        Skill result = skillService.createSkill(testSkill);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Java", result.getName());
        verify(skillRepository).save(testSkill);
    }

    @Test
    void findSkillById_WhenSkillExists_ShouldReturnSkill() {
        // Arrange
        when(skillRepository.findById("1")).thenReturn(Optional.of(testSkill));

        // Act
        Optional<Skill> result = skillService.findSkillById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("Java", result.get().getName());
        verify(skillRepository).findById("1");
    }

    @Test
    void findSkillById_WhenSkillDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(skillRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Skill> result = skillService.findSkillById("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        verify(skillRepository).findById("nonexistent");
    }

    @Test
    void getAllSkills_ShouldReturnAllSkills() {
        // Arrange
        Skill skill2 = new Skill();
        skill2.setId("2");
        skill2.setName("Python");
        
        List<Skill> skills = Arrays.asList(testSkill, skill2);
        when(skillRepository.findAll()).thenReturn(skills);

        // Act
        List<Skill> result = skillService.getAllSkills();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Python", result.get(1).getName());
        verify(skillRepository).findAll();
    }

    @Test
    void updateSkill_WhenSkillExists_ShouldUpdateAndReturnSkill() {
        // Arrange
        Skill updatedSkill = new Skill();
        updatedSkill.setName("Java Updated");
        
        when(skillRepository.findById("1")).thenReturn(Optional.of(testSkill));
        when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
            Skill savedSkill = invocation.getArgument(0);
            return savedSkill;
        });

        // Act
        Optional<Skill> result = skillService.updateSkill("1", updatedSkill);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("Java Updated", result.get().getName());
        verify(skillRepository).findById("1");
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    void updateSkill_WhenSkillDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        Skill updatedSkill = new Skill();
        updatedSkill.setName("Java Updated");
        
        when(skillRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Skill> result = skillService.updateSkill("nonexistent", updatedSkill);

        // Assert
        assertFalse(result.isPresent());
        verify(skillRepository).findById("nonexistent");
        verify(skillRepository, never()).save(any(Skill.class));
    }

    @Test
    void deleteSkill_WhenSkillExists_ShouldReturnTrue() {
        // Arrange
        when(skillRepository.deleteById("1")).thenReturn(true);

        // Act
        boolean result = skillService.deleteSkill("1");

        // Assert
        assertTrue(result);
        verify(skillRepository).deleteById("1");
    }

    @Test
    void deleteSkill_WhenSkillDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(skillRepository.deleteById(anyString())).thenReturn(false);

        // Act
        boolean result = skillService.deleteSkill("nonexistent");

        // Assert
        assertFalse(result);
        verify(skillRepository).deleteById("nonexistent");
    }
}