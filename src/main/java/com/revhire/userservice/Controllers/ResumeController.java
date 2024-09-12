package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.ResumeService;
import com.revhire.userservice.models.Resume;
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
}
