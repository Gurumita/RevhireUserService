package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.ExperienceService;
import com.revhire.userservice.models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @PostMapping("/create")
    public ResponseEntity<Experience> createExperience(@RequestBody Experience experience) {
        Experience createdExperience = experienceService.createExperience(experience);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Experience>> getAllExperiences() {
        List<Experience> experienceList = experienceService.getAllExperiences();
        return new ResponseEntity<>(experienceList, HttpStatus.OK);
    }
}
