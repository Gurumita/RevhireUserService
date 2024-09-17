package com.revhire.userservice.Controllers;


import com.revhire.userservice.Services.ApplicationService;
import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PutMapping("/updateStatus/{userId}/{jobId}")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long userId,
            @PathVariable Long jobId,
            @RequestParam ApplicationStatus status) {
        Application updatedApplication = applicationService.updateApplicationStatus(userId, jobId, status);
        return ResponseEntity.ok(updatedApplication);
    }
}

