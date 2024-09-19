package com.revhire.userservice.Services;

import com.revhire.userservice.dto.Resume;
import com.revhire.userservice.models.*;
import com.revhire.userservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final SkillsRepository skillsRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final LanguageRepository languageRepository;
    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResumeService(SkillsRepository skillsRepository, EducationRepository educationRepository,
                         ExperienceRepository experienceRepository, LanguageRepository languageRepository,
                         SummaryRepository summaryRepository, UserRepository userRepository) {
        this.skillsRepository = skillsRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
        this.languageRepository = languageRepository;
        this.summaryRepository = summaryRepository;
        this.userRepository = userRepository;
    }

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

    public void saveResume(Long userId, Resume resume) {
        // Find the user
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();

        // Save skills
        for (Skills skill : resume.getSkills()) {
            skill.setUser(user);
            skillsRepository.save(skill);
        }

        // Save education
        for (Education edu : resume.getEducation()) {
            edu.setUser(user);
            educationRepository.save(edu);
        }

        // Save experience
        for (Experience exp : resume.getExperience()) {
            exp.setUser(user);
            experienceRepository.save(exp);
        }

        // Save languages
        for (Language lang : resume.getLanguages()) {
            lang.setUser(user);
            languageRepository.save(lang);
        }

        // Save summary
        Summary summary = resume.getSummary();
        summary.setUser(user);
        summaryRepository.save(summary);
    }

    public void updateResume(Long userId, Resume resume) {
        // Ensure user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        // Update skills
        List<Skills> existingSkills = skillsRepository.findAll().stream()
                .filter(skill -> skill.getUser().getUserId() == userId)
                .toList();
        skillsRepository.deleteAll(existingSkills);
        for (Skills skill : resume.getSkills()) {
            skill.setUser(userOptional.get());
            skillsRepository.save(skill);
        }

        // Update education
        List<Education> existingEducation = educationRepository.findAll().stream()
                .filter(edu -> edu.getUser().getUserId() == userId)
                .toList();
        educationRepository.deleteAll(existingEducation);
        for (Education edu : resume.getEducation()) {
            edu.setUser(userOptional.get());
            educationRepository.save(edu);
        }

        // Update experience
        List<Experience> existingExperience = experienceRepository.findAll().stream()
                .filter(exp -> exp.getUser().getUserId() == userId)
                .toList();
        experienceRepository.deleteAll(existingExperience);
        for (Experience exp : resume.getExperience()) {
            exp.setUser(userOptional.get());
            experienceRepository.save(exp);
        }

        // Update languages
        List<Language> existingLanguages = languageRepository.findAll().stream()
                .filter(lang -> lang.getUser().getUserId() == userId)
                .toList();
        languageRepository.deleteAll(existingLanguages);
        for (Language lang : resume.getLanguages()) {
            lang.setUser(userOptional.get());
            languageRepository.save(lang);
        }

        // Update summary
        Summary existingSummary = summaryRepository.findAll().stream()
                .filter(sum -> sum.getUser().getUserId() == userId)
                .findFirst()
                .orElse(null);
        if (existingSummary != null) {
            summaryRepository.delete(existingSummary);
        }
        Summary newSummary = resume.getSummary();
        newSummary.setUser(userOptional.get());
        summaryRepository.save(newSummary);
    }
}
