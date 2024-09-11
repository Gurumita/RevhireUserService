package com.revhire.userservice.Services;


import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Job;
import com.revhire.userservice.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> searchJobs(String jobTitle, String location, ExperienceRequired experienceRequired, String skillsRequired, String companyName) {
        return jobRepository.findJobs(jobTitle, location, experienceRequired, skillsRequired, companyName);
    }
}
