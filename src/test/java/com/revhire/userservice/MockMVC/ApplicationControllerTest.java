package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.ApplicationController;
import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.Services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testUpdateApplicationStatus_Success() throws Exception {
        Application application = new Application();
        application.setApplicationId(1L);
        application.setStatus(ApplicationStatus.APPLIED);

        when(applicationService.updateApplicationStatus(any(Long.class), any(Long.class), any(ApplicationStatus.class)))
                .thenReturn(application);

        mockMvc.perform(put("/api/applications/updateStatus/{userId}/{jobId}", 1L, 1L)
                        .param("status", ApplicationStatus.APPLIED.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(1L))
                .andExpect(jsonPath("$.status").value(ApplicationStatus.APPLIED.name()));
    }

}
