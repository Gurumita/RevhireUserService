package com.revhire.userservice.Controllers;


import com.revhire.userservice.Services.JobService;
import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Application;
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
    @PostMapping("/{jobId}/apply/{userId}")
    public void applyForJob(@PathVariable Long jobId, @PathVariable Long userId) {

        jobService.applyForJob(jobId, userId);

    }
    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
    @GetMapping("/user/{userId}/applications")
    public ResponseEntity<List<Application>> getUserApplications(@PathVariable Long userId) {
        List<Application> applications = jobService.getUserApplications(userId);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }
    @PostMapping("/{jobId}/withdraw/{userId}")
    public ResponseEntity<String> withdrawApplication(@PathVariable Long jobId, @PathVariable Long userId) {
        try {
            jobService.withdrawApplication(jobId, userId);
            return new ResponseEntity<>("Application withdrawn successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
