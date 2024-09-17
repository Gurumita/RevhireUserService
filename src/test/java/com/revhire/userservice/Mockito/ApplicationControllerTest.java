package com.revhire.userservice.Mockito;

import com.revhire.userservice.Controllers.ApplicationController;
import com.revhire.userservice.Services.ApplicationService;
import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    public ApplicationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateApplicationStatus() {
        Long userId = 1L;
        Long jobId = 1L;
        ApplicationStatus status = ApplicationStatus.APPLIED;
        Application mockApplication = new Application();

        when(applicationService.updateApplicationStatus(userId, jobId, status)).thenReturn(mockApplication);

        ResponseEntity<Application> response = applicationController.updateApplicationStatus(userId, jobId, status);

        assertEquals(ResponseEntity.ok(mockApplication), response);
        verify(applicationService, times(1)).updateApplicationStatus(userId, jobId, status);
    }
}


