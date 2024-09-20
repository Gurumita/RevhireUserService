package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.SkillsController;
import com.revhire.userservice.Services.SkillsService;
import com.revhire.userservice.models.Skills;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SkillsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SkillsService skillsService;

    @InjectMocks
    private SkillsController skillsController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skillsController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateSkill_Success() throws Exception {
        Skills skill = new Skills();
        skill.setSkillName("Java");
        skill.setSkillDescription("Object-oriented programming language");

        when(skillsService.createSkill(any(Skills.class))).thenReturn(skill);

        mockMvc.perform(post("/api/skills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skill)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.skillName").value("Java"))
                .andExpect(jsonPath("$.skillDescription").value("Object-oriented programming language"));
    }

    @Test
    public void testGetAllSkills_Success() throws Exception {
        Skills skill = new Skills();
        skill.setSkillName("Java");
        skill.setSkillDescription("Object-oriented programming language");

        List<Skills> skillsList = Collections.singletonList(skill);

        when(skillsService.getAllSkills()).thenReturn(skillsList);

        mockMvc.perform(get("/api/skills/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skillName").value("Java"))
                .andExpect(jsonPath("$[0].skillDescription").value("Object-oriented programming language"));
    }
}
