package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.ExperienceService;
import com.revhire.userservice.models.Experience;
import com.revhire.userservice.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperienceService experienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExperience() {
        Experience experience = new Experience();
        experience.setExperienceId(1L);
        experience.setJobPosition("Software Engineer");
        experience.setOfficeName("Tech Corp");
        experience.setStartDate(new Date());
        experience.setEndDate(new Date());

        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);

        Experience createdExperience = experienceService.createExperience(experience);

        assertNotNull(createdExperience);
        assertEquals("Software Engineer", createdExperience.getJobPosition());
        assertEquals("Tech Corp", createdExperience.getOfficeName());
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void getAllExperiences() {
        Experience experience1 = new Experience();
        Experience experience2 = new Experience();
        List<Experience> experiences = Arrays.asList(experience1, experience2);

        when(experienceRepository.findAll()).thenReturn(experiences);

        List<Experience> result = experienceService.getAllExperiences();

        assertEquals(2, result.size());
        verify(experienceRepository, times(1)).findAll();
    }
}
