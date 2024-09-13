package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.SkillsService;
import com.revhire.userservice.models.Skills;
import com.revhire.userservice.repository.SkillsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SkillsServiceTest {

    @Mock
    private SkillsRepository skillsRepository;

    @InjectMocks
    private SkillsService skillsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSkill() {
        Skills skill = new Skills();
        skill.setSkillId(1L);
        skill.setSkillName("Java");
        skill.setSkillDescription("Programming Language");

        when(skillsRepository.save(any(Skills.class))).thenReturn(skill);

        Skills createdSkill = skillsService.createSkill(skill);

        assertNotNull(createdSkill);
        assertEquals("Java", createdSkill.getSkillName());
        assertEquals("Programming Language", createdSkill.getSkillDescription());
        verify(skillsRepository, times(1)).save(skill);
    }

    @Test
    void getAllSkills() {
        Skills skill1 = new Skills();
        Skills skill2 = new Skills();
        List<Skills> skills = Arrays.asList(skill1, skill2);

        when(skillsRepository.findAll()).thenReturn(skills);

        List<Skills> result = skillsService.getAllSkills();

        assertEquals(2, result.size());
        verify(skillsRepository, times(1)).findAll();
    }
}
