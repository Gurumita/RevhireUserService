package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.JobService;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        try {
            Job createdJob = jobService.createJob(job);
            return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Long jobId) {
        Optional<Job> job = jobService.getJobById(jobId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/{jobId}/apply/{userId}")
    public void applyForJob(@PathVariable Long jobId, @PathVariable Long userId) {
        jobService.applyForJob(jobId, userId);
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

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<Job>> getJobsByEmployerId(@PathVariable Long employerId) {
        List<Job> jobs = jobService.getJobsByEmployerId(employerId);
        if (jobs.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no jobs found
        }
        return ResponseEntity.ok(jobs); // 200 OK with list of jobs
    }

    @GetMapping("/not-applied/{userId}")
    public List<Job> getJobsNotAppliedByUser(@PathVariable Long userId) {
        return jobService.getJobsNotAppliedByUser(userId);
    }

}