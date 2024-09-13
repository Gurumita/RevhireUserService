package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.EducationService;
import com.revhire.userservice.models.Education;
import com.revhire.userservice.repository.EducationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EducationServiceTest {

    @Mock
    private EducationRepository educationRepository;

    @InjectMocks
    private EducationService educationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEducation() {
        Education education = new Education();
        education.setEducationId(1L);
        education.setDegree("BSc Computer Science");
        education.setInstitution("XYZ University");
        education.setStartYear(2015);
        education.setEndYear(2019);

        when(educationRepository.save(any(Education.class))).thenReturn(education);

        Education createdEducation = educationService.createEducation(education);

        assertNotNull(createdEducation);
        assertEquals("BSc Computer Science", createdEducation.getDegree());
        assertEquals("XYZ University", createdEducation.getInstitution());
        assertEquals(2015, createdEducation.getStartYear());
        assertEquals(2019, createdEducation.getEndYear());
        verify(educationRepository, times(1)).save(education);
    }

    @Test
    void getAllEducation() {
        Education education1 = new Education();
        Education education2 = new Education();
        List<Education> educationList = Arrays.asList(education1, education2);

        when(educationRepository.findAll()).thenReturn(educationList);

        List<Education> result = educationService.getAllEducation();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(educationRepository, times(1)).findAll();
    }
}
