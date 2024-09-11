package com.revhire.userservice.Controllers;


import com.revhire.userservice.Services.JobService;
import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) ExperienceRequired experienceRequired,
            @RequestParam(required = false) String skillsRequired,
            @RequestParam(required = false) String companyName) {

        List<Job> jobs = jobService.searchJobs(jobTitle, location, experienceRequired, skillsRequired, companyName);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
