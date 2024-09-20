package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.EducationController;
import com.revhire.userservice.models.Education;
import com.revhire.userservice.Services.EducationService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class EducationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EducationService educationService;

    @InjectMocks
    private EducationController educationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(educationController).build();
    }

    @Test
    void testCreateEducation() throws Exception {
        Education education = new Education(1L, "Bachelor's Degree", "University", 2015, 2019, null);
        when(educationService.createEducation(any(Education.class))).thenReturn(education);

        mockMvc.perform(post("/api/education/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(education)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.degree").value("Bachelor's Degree"));
    }

    @Test
    void testGetAllEducation() throws Exception {
        Education education1 = new Education(1L, "Bachelor's Degree", "University", 2015, 2019, null);
        Education education2 = new Education(2L, "Master's Degree", "College", 2020, 2022, null);
        List<Education> educationList = Arrays.asList(education1, education2);

        when(educationService.getAllEducation()).thenReturn(educationList);

        mockMvc.perform(get("/api/education/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
