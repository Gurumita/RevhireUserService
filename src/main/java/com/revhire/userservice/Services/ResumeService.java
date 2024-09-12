package com.revhire.userservice.Services;

import com.revhire.userservice.models.*;
import com.revhire.userservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private UserRepository userRepository;

    public Resume getResumeByUserId(Long userId) {
        Resume resume = new Resume();

        // Retrieve user information
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            resume.setUser(user);

            // Retrieve and set skills
            List<Skills> skills = skillsRepository.findAll().stream()
                    .filter(skill -> skill.getUser().getUserId() == userId)
                    .toList();
            resume.setSkills(skills);

            // Retrieve and set education
            List<Education> education = educationRepository.findAll().stream()
                    .filter(edu -> edu.getUser().getUserId() == userId)
                    .toList();
            resume.setEducation(education);

            // Retrieve and set experience
            List<Experience> experience = experienceRepository.findAll().stream()
                    .filter(exp -> exp.getUser().getUserId() == userId)
                    .toList();
            resume.setExperience(experience);

            // Retrieve and set languages
            List<Language> languages = languageRepository.findAll().stream()
                    .filter(lang -> lang.getUser().getUserId() == userId)
                    .toList();
            resume.setLanguages(languages);

            // Retrieve and set summary
            Summary summary = summaryRepository.findAll().stream()
                    .filter(sum -> sum.getUser().getUserId() == userId)
                    .findFirst()
                    .orElse(null);
            resume.setSummary(summary);
        }

        return resume;
    }
}
