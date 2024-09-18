package com.revhire.userservice.Services;

import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application updateApplicationStatus(Long userId, Long jobId, ApplicationStatus newStatus) {
        Optional<Application> applicationOpt = applicationRepository.findByUserUserIdAndJobJobId(userId, jobId);

        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(newStatus);
            return applicationRepository.save(application);
        } else {
            throw new RuntimeException("Application not found for the given userId and jobId");
        }
    }
}

