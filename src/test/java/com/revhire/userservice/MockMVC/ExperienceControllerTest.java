package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.ExperienceController;
import com.revhire.userservice.models.Experience;
import com.revhire.userservice.Services.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ExperienceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceController experienceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(experienceController).build();
    }

    @Test
    void testCreateExperience() throws Exception {
        Experience experience = new Experience(1L, "Software Engineer", "Tech Company", new Date(), null, null);
        when(experienceService.createExperience(any(Experience.class))).thenReturn(experience);

        mockMvc.perform(post("/api/experience/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(experience)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobPosition").value("Software Engineer"));
    }

    @Test
    void testGetAllExperiences() throws Exception {
        Experience experience1 = new Experience(1L, "Software Engineer", "Tech Company", new Date(), null, null);
        Experience experience2 = new Experience(2L, "Project Manager", "Another Company", new Date(), null, null);
        List<Experience> experiences = Arrays.asList(experience1, experience2);

        when(experienceService.getAllExperiences()).thenReturn(experiences);

        mockMvc.perform(get("/api/experience/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
