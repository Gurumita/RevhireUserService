package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.ResumeController;
import com.revhire.userservice.dto.Resume;
import com.revhire.userservice.models.User;
import com.revhire.userservice.Services.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ResumeService resumeService;

    @InjectMocks
    private ResumeController resumeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(resumeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetResumeByUserId_Success() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User());
        resume.setSkills(Collections.emptyList());

        when(resumeService.getResumeByUserId(1L)).thenReturn(resume);

        mockMvc.perform(get("/api/resumes/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").exists()); // Adjust based on your Resume structure
    }

    @Test
    public void testGetResumeByUserId_NotFound() throws Exception {
        when(resumeService.getResumeByUserId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/resumes/user/{userId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSubmitResume_Success() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User()); // Populate the resume as needed

        mockMvc.perform(post("/api/resumes/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resume)))
                .andExpect(status().isOk())
                .andExpect(content().string("Resume submitted successfully!"));

        verify(resumeService, times(1)).saveResume(eq(1L), any(Resume.class));
    }

    @Test
    public void testSubmitResume_Error() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User()); // Populate the resume as needed

        doThrow(new RuntimeException("Error submitting resume")).when(resumeService).saveResume(eq(1L), any(Resume.class));

        mockMvc.perform(post("/api/resumes/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resume)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error submitting resume"));
    }

    @Test
    public void testUpdateResume_Success() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User()); // Populate as needed

        mockMvc.perform(put("/api/resumes/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resume)))
                .andExpect(status().isOk())
                .andExpect(content().string("Resume updated successfully"));

        verify(resumeService, times(1)).updateResume(eq(1L), any(Resume.class));
    }

    @Test
    public void testUpdateResume_UserNotFound() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User()); // Populate as needed

        doThrow(new IllegalArgumentException("User not found")).when(resumeService).updateResume(eq(1L), any(Resume.class));

        mockMvc.perform(put("/api/resumes/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resume)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testUpdateResume_Error() throws Exception {
        Resume resume = new Resume();
        resume.setUser(new User()); // Populate as needed

        doThrow(new RuntimeException("An error occurred")).when(resumeService).updateResume(eq(1L), any(Resume.class));

        mockMvc.perform(put("/api/resumes/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resume)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred"));
    }
}
