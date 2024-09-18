package com.revhire.userservice.Services;

import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.models.Job;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.ApplicationRepository;
import com.revhire.userservice.repository.EmployerRepository;
import com.revhire.userservice.repository.JobRepository;
import com.revhire.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, EmployerRepository employerRepository,
                      UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public void applyForJob(Long jobId, Long userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = new Application();
        application.setJob(job);
        application.setUser(user);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setApplicationDate(new Date());

        applicationRepository.save(application);
        job.getApplicants().add(user);
        jobRepository.save(job);
    }

    public List<Application> getUserApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByUser(user);
    }

    public void withdrawApplication(Long jobId, Long userId) {
        Application application = applicationRepository.findByJob_JobIdAndUser_UserId(jobId, userId);

        if (application != null) {
            application.setStatus(ApplicationStatus.WITHDRAWN);
            applicationRepository.save(application);
        } else {
            throw new RuntimeException("Application not found");
        }
    }

    public List<Job> getJobsByEmployerId(Long employerId) {
        return jobRepository.findByEmployer_EmpolyerId(employerId);
    }

    public List<Job> getJobsNotAppliedByUser(Long userId) {
        return jobRepository.findJobsNotAppliedByUser(userId);
    }

}