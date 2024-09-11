package com.revhire.userservice.Services;


import com.revhire.userservice.models.Experience;
import com.revhire.userservice.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    public Experience createExperience(Experience experience) {
        return experienceRepository.save(experience);
    }

    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }
}

