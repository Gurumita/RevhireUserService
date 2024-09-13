package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.SummaryService;
import com.revhire.userservice.models.Summary;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.SummaryRepository;
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

public class SummaryServiceTest {

    @Mock
    private SummaryRepository summaryRepository;

    @InjectMocks
    private SummaryService summaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSummary() {
        Summary summary = new Summary();
        summary.setSummaryId(1L);
        summary.setSummaryText("Test Summary");
        summary.setUser(null); // assuming user is not set

        when(summaryRepository.save(any(Summary.class))).thenReturn(summary);

        Summary createdSummary = summaryService.createSummary(summary);

        assertNotNull(createdSummary);
        assertEquals("Test Summary", createdSummary.getSummaryText());
        verify(summaryRepository, times(1)).save(summary);
    }

    @Test
    void getAllSummaries() {
        Summary summary1 = new Summary();
        Summary summary2 = new Summary();
        List<Summary> summaries = Arrays.asList(summary1, summary2);

        when(summaryRepository.findAll()).thenReturn(summaries);

        List<Summary> result = summaryService.getAllSummaries();

        assertEquals(2, result.size());
        verify(summaryRepository, times(1)).findAll();
    }

    @Test
    void getSummaryByUserId() {
        Summary summary = new Summary();
        summary.setSummaryId(1L);
        summary.setUser(new User()); // assuming User class with userId
        summary.getUser().setUserId(1L);

        when(summaryRepository.findAll()).thenReturn(Arrays.asList(summary));

        Summary result = summaryService.getSummaryByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUser().getUserId());
        verify(summaryRepository, times(1)).findAll();
    }
}
