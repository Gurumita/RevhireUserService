package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.JobController;
import com.revhire.userservice.Services.JobService;
import com.revhire.userservice.models.Job;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
public class JobControllerWebMvcTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateJob() throws Exception {
        Job mockJob = new Job();
        Mockito.when(jobService.createJob(Mockito.any(Job.class))).thenReturn(mockJob);

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockJob)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetJobById_Found() throws Exception {
        Job mockJob = new Job();
        Mockito.when(jobService.getJobById(1L)).thenReturn(Optional.of(mockJob));

        mockMvc.perform(get("/api/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testGetJobById_NotFound() throws Exception {
        Mockito.when(jobService.getJobById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllJobs() throws Exception {
        Mockito.when(jobService.getAllJobs())
                .thenReturn(Arrays.asList(new Job(), new Job()));

        mockMvc.perform(get("/api/jobs/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testApplyForJob() throws Exception {
        Mockito.doNothing().when(jobService).applyForJob(1L, 1L);

        mockMvc.perform(post("/api/jobs/1/apply/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdrawApplication_Success() throws Exception {
        Mockito.doNothing().when(jobService).withdrawApplication(1L, 1L);

        mockMvc.perform(post("/api/jobs/1/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdrawApplication_Failure() throws Exception {
        Mockito.doThrow(new RuntimeException("Application not found"))
                .when(jobService).withdrawApplication(1L, 1L);

        mockMvc.perform(post("/api/jobs/1/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetJobsByEmployerId_Found() throws Exception {
        Mockito.when(jobService.getJobsByEmployerId(1L))
                .thenReturn(Arrays.asList(new Job(), new Job()));

        mockMvc.perform(get("/api/jobs/employer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetJobsByEmployerId_NoContent() throws Exception {
        Mockito.when(jobService.getJobsByEmployerId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/jobs/employer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}


