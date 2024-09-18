//package com.revhire.userservice.Mockito;
//
//import com.revhire.userservice.Services.JobService;
//import com.revhire.userservice.enums.ApplicationStatus;
//import com.revhire.userservice.enums.ExperienceRequired;
//import com.revhire.userservice.models.Application;
//import com.revhire.userservice.models.Job;
//import com.revhire.userservice.models.User;
//import com.revhire.userservice.repository.ApplicationRepository;
//import com.revhire.userservice.repository.JobRepository;
//import com.revhire.userservice.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class JobServiceTest {
//
//    @InjectMocks
//    private JobService jobService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ApplicationRepository applicationRepository;
//
//    @Mock
//    private JobRepository jobRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetUserApplications() {
//        Long userId = 1L;
//
//        User user = new User();
//        user.setUserId(userId);
//
//        Application application = new Application();
//        application.setUser(user);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(applicationRepository.findByUser(user)).thenReturn(List.of(application));
//
//        List<Application> applications = jobService.getUserApplications(userId);
//
//        assertNotNull(applications);
//        assertEquals(1, applications.size());
//        assertEquals(userId, applications.get(0).getUser().getUserId());
//    }
//
//    @Test
//    void testGetAllJobs() {
//        Job job = new Job();
//        job.setJobTitle("Software Engineer");
//
//        when(jobRepository.findAll()).thenReturn(List.of(job));
//
//        List<Job> jobs = jobService.getAllJobs();
//
//        assertNotNull(jobs);
//        assertEquals(1, jobs.size());
//        assertEquals("Software Engineer", jobs.get(0).getJobTitle());
//    }
//
//    @Test
//    void testWithdrawApplication() {
//        Long jobId = 1L;
//        Long userId = 1L;
//
//        Application application = new Application();
//        application.setJob(new Job());
//        application.setUser(new User());
//        application.setStatus(ApplicationStatus.APPLIED);
//
//        when(applicationRepository.findByJob_JobIdAndUser_UserId(jobId, userId)).thenReturn(application);
//
//        jobService.withdrawApplication(jobId, userId);
//
//        application.setStatus(ApplicationStatus.WITHDRAWN);
//        verify(applicationRepository, times(1)).save(application);
//    }
//
//    @Test
//    void testWithdrawApplicationNotFound() {
//        Long jobId = 1L;
//        Long userId = 1L;
//
//        when(applicationRepository.findByJob_JobIdAndUser_UserId(jobId, userId)).thenReturn(null);
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jobService.withdrawApplication(jobId, userId));
//        assertEquals("Application not found", thrown.getMessage());
//    }
//}
