package com.revhire.userservice.Mockito;

import com.revhire.userservice.Controllers.JobController;
import com.revhire.userservice.Services.JobService;
import com.revhire.userservice.models.Job;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    public JobControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateJob_Success() {
        Job mockJob = new Job();
        when(jobService.createJob(mockJob)).thenReturn(mockJob);

        ResponseEntity<Job> response = jobController.createJob(mockJob);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockJob, response.getBody());
        verify(jobService, times(1)).createJob(mockJob);
    }

    @Test
    public void testCreateJob_Failure() {
        Job mockJob = new Job();
        when(jobService.createJob(mockJob)).thenThrow(RuntimeException.class);

        ResponseEntity<Job> response = jobController.createJob(mockJob);

        assertEquals(400, response.getStatusCodeValue());
        verify(jobService, times(1)).createJob(mockJob);
    }

    @Test
    public void testGetJobById_Found() {
        Job mockJob = new Job();
        when(jobService.getJobById(1L)).thenReturn(Optional.of(mockJob));

        ResponseEntity<Job> response = jobController.getJobById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockJob, response.getBody());
        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    public void testGetJobById_NotFound() {
        when(jobService.getJobById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.getJobById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    public void testGetAllJobs() {
        List<Job> mockJobs = Arrays.asList(new Job(), new Job());
        when(jobService.getAllJobs()).thenReturn(mockJobs);

        ResponseEntity<List<Job>> response = jobController.getAllJobs();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockJobs, response.getBody());
        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    public void testApplyForJob() {
        Long jobId = 1L;
        Long userId = 1L;

        doNothing().when(jobService).applyForJob(jobId, userId);

        jobController.applyForJob(jobId, userId);

        verify(jobService, times(1)).applyForJob(jobId, userId);
    }

    @Test
    public void testWithdrawApplication_Success() {
        Long jobId = 1L;
        Long userId = 1L;

        doNothing().when(jobService).withdrawApplication(jobId, userId);

        ResponseEntity<String> response = jobController.withdrawApplication(jobId, userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Application withdrawn successfully", response.getBody());
        verify(jobService, times(1)).withdrawApplication(jobId, userId);
    }

    @Test
    public void testWithdrawApplication_Failure() {
        Long jobId = 1L;
        Long userId = 1L;

        doThrow(new RuntimeException("Application not found")).when(jobService).withdrawApplication(jobId, userId);

        ResponseEntity<String> response = jobController.withdrawApplication(jobId, userId);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Application not found", response.getBody());
        verify(jobService, times(1)).withdrawApplication(jobId, userId);
    }

    @Test
    public void testGetJobsByEmployerId_Found() {
        List<Job> mockJobs = Arrays.asList(new Job(), new Job());
        when(jobService.getJobsByEmployerId(1L)).thenReturn(mockJobs);

        ResponseEntity<List<Job>> response = jobController.getJobsByEmployerId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockJobs, response.getBody());
        verify(jobService, times(1)).getJobsByEmployerId(1L);
    }

    @Test
    public void testGetJobsByEmployerId_NoContent() {
        when(jobService.getJobsByEmployerId(1L)).thenReturn(Arrays.asList());

        ResponseEntity<List<Job>> response = jobController.getJobsByEmployerId(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(jobService, times(1)).getJobsByEmployerId(1L);
    }
}


