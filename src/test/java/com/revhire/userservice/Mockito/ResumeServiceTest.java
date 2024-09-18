//package com.revhire.userservice.Mockito;
//
//import com.revhire.userservice.Services.ResumeService;
//import com.revhire.userservice.models.*;
//import com.revhire.userservice.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class ResumeServiceTest {
//
//    @InjectMocks
//    private ResumeService resumeService;
//
//    @Mock
//    private SkillsRepository skillsRepository;
//
//    @Mock
//    private EducationRepository educationRepository;
//
//    @Mock
//    private ExperienceRepository experienceRepository;
//
//    @Mock
//    private LanguageRepository languageRepository;
//
//    @Mock
//    private SummaryRepository summaryRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetResumeByUserId() {
//        Long userId = 1L;
//
//        // Mock User
//        User user = new User();
//        user.setUserId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Mock Skills
//        List<Skills> skills = new ArrayList<>();
//        skills.add(new Skills(1L, "Java", "Programming Language", user));
//        when(skillsRepository.findAll()).thenReturn(skills);
//
//        // Mock Education
//        List<Education> education = new ArrayList<>();
//        education.add(new Education(1L, "BSc Computer Science", "ABC University", 2010, 2014, user));
//        when(educationRepository.findAll()).thenReturn(education);
//
//        // Mock Experience
//        List<Experience> experience = new ArrayList<>();
//        experience.add(new Experience(1L, "Software Developer", "XYZ Ltd.", null, null, user));
//        when(experienceRepository.findAll()).thenReturn(experience);
//
//        // Mock Language
//        List<Language> languages = new ArrayList<>();
//        languages.add(new Language(1L, "English", "Advanced", user));
//        when(languageRepository.findAll()).thenReturn(languages);
//
//        // Mock Summary
//        Summary summary = new Summary(1L, "Experienced developer with a passion for technology.", user);
//        when(summaryRepository.findAll()).thenReturn(List.of(summary));
//
//        // Call the method under test
//        Resume resume = resumeService.getResumeByUserId(userId);
//
//        // Verify the result
//        assertEquals(user, resume.getUser());
//        assertEquals(skills, resume.getSkills());
//        assertEquals(education, resume.getEducation());
//        assertEquals(experience, resume.getExperience());
//        assertEquals(languages, resume.getLanguages());
//        assertEquals(summary, resume.getSummary());
//
//        // Verify interactions
//        verify(userRepository, times(1)).findById(userId);
//        verify(skillsRepository, times(1)).findAll();
//        verify(educationRepository, times(1)).findAll();
//        verify(experienceRepository, times(1)).findAll();
//        verify(languageRepository, times(1)).findAll();
//        verify(summaryRepository, times(1)).findAll();
//    }
//}
