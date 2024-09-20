package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.SummaryController;
import com.revhire.userservice.Services.SummaryService;
import com.revhire.userservice.models.Summary;
import com.revhire.userservice.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SummaryService summaryService;

    @InjectMocks
    private SummaryController summaryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(summaryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateSummary_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);

        Summary summary = new Summary();
        summary.setSummaryText("This is a summary");
        summary.setUser(user);

        when(summaryService.createSummary(any(Summary.class))).thenReturn(summary);

        mockMvc.perform(post("/api/summaries/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(summary)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summaryText").value("This is a summary"));
    }

    @Test
    public void testGetAllSummaries_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);

        Summary summary = new Summary();
        summary.setSummaryText("This is a summary");
        summary.setUser(user);

        List<Summary> summaries = Collections.singletonList(summary);

        when(summaryService.getAllSummaries()).thenReturn(summaries);

        mockMvc.perform(get("/api/summaries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].summaryText").value("This is a summary"));
    }

    @Test
    public void testGetSummaryByUserId_Success() throws Exception {
        User user = new User();
        user.setUserId(1L);

        Summary summary = new Summary();
        summary.setSummaryText("This is a summary");
        summary.setUser(user);

        when(summaryService.getSummaryByUserId(1L)).thenReturn(summary);

        mockMvc.perform(get("/api/summaries/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryText").value("This is a summary"));
    }

    @Test
    public void testGetSummaryByUserId_NotFound() throws Exception {
        when(summaryService.getSummaryByUserId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/summaries/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
