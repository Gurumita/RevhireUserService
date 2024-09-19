package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.ResumeService;
import com.revhire.userservice.dto.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Resume> getResumeByUserId(@PathVariable Long userId) {
        Resume resume = resumeService.getResumeByUserId(userId);
        if (resume != null) {
            return new ResponseEntity<>(resume, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> submitResume(@PathVariable Long userId, @RequestBody Resume resume) {
        try {
            resumeService.saveResume(userId, resume);
            return new ResponseEntity<>("Resume submitted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error submitting resume", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<String> updateResume(@PathVariable Long userId, @RequestBody Resume resume) {
        try {
            resumeService.updateResume(userId, resume);
            return new ResponseEntity<>("Resume updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
