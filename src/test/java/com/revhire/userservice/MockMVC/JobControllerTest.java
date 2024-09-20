package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.JobController;
import com.revhire.userservice.models.Job;
import com.revhire.userservice.Services.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateJob() throws Exception {
        Job job = new Job(); // Set fields as needed
        job.setJobTitle("Software Engineer");

        when(jobService.createJob(any(Job.class))).thenReturn(job);

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobTitle").value("Software Engineer"));
    }

    @Test
    public void testGetJobById() throws Exception {
        Job job = new Job(); // Set fields as needed
        job.setJobId(1L);
        job.setJobTitle("Software Engineer");

        when(jobService.getJobById(1L)).thenReturn(Optional.of(job));

        mockMvc.perform(get("/api/jobs/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle").value("Software Engineer"));
    }

    @Test
    public void testGetAllJobs() throws Exception {
        Job job1 = new Job(); // Set fields as needed
        job1.setJobId(1L);
        job1.setJobTitle("Software Engineer");

        Job job2 = new Job(); // Set fields as needed
        job2.setJobId(2L);
        job2.setJobTitle("Data Scientist");

        when(jobService.getAllJobs()).thenReturn(Arrays.asList(job1, job2));

        mockMvc.perform(get("/api/jobs/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobTitle").value("Software Engineer"))
                .andExpect(jsonPath("$[1].jobTitle").value("Data Scientist"));
    }

    @Test
    public void testApplyForJob() throws Exception {
        // Assume applyForJob method does not return anything, so we check for a 200 status
        mockMvc.perform(post("/api/jobs/{jobId}/apply/{userId}", 1L, 1L))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(jobService, times(1)).applyForJob(1L, 1L);
    }

    @Test
    public void testWithdrawApplication() throws Exception {
        doNothing().when(jobService).withdrawApplication(1L, 1L);

        mockMvc.perform(post("/api/jobs/{jobId}/withdraw/{userId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Application withdrawn successfully"));

        verify(jobService, times(1)).withdrawApplication(1L, 1L);
    }
}
