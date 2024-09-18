package com.revhire.userservice.MockMVC;

import com.revhire.userservice.Controllers.ApplicationController;
import com.revhire.userservice.Services.ApplicationService;
import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Test
    public void testUpdateApplicationStatus() throws Exception {
        Application mockApplication = new Application();
        Mockito.when(applicationService.updateApplicationStatus(any(Long.class), any(Long.class), any(ApplicationStatus.class)))
                .thenReturn(mockApplication);

        mockMvc.perform(put("/api/applications/updateStatus/1/1")
                        .param("status", "APPLIED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


