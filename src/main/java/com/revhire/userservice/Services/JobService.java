package com.revhire.userservice.Services;


import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.models.Job;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.ApplicationRepository;
import com.revhire.userservice.repository.JobRepository;
import com.revhire.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Job> searchJobs(String jobTitle, String location, ExperienceRequired experienceRequired, String skillsRequired, String companyName) {
        return jobRepository.findJobs(jobTitle, location, experienceRequired, skillsRequired, companyName);
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
    }

    public List<Application> getUserApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByUser(user);
    }
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
