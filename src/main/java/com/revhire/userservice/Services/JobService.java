package com.revhire.userservice.Services;


import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Job;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.JobRepository;
import com.revhire.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Job> searchJobs(String jobTitle, String location, ExperienceRequired experienceRequired, String skillsRequired, String companyName) {
        return jobRepository.findJobs(jobTitle, location, experienceRequired, skillsRequired, companyName);
    }
    public void applyForJob(Long jobId, Long userId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        User user = (User) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        job.getApplicants().add(user);
        jobRepository.save(job);
    }
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
