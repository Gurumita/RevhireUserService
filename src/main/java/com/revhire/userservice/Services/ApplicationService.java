package com.revhire.userservice.Services;

import com.revhire.userservice.enums.ApplicationStatus;
import com.revhire.userservice.models.Application;
import com.revhire.userservice.repository.ApplicationRepository;
import com.revhire.userservice.utilities.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;

    public Application updateApplicationStatus(Long userId, Long jobId, ApplicationStatus newStatus) {
        Optional<Application> applicationOpt = applicationRepository.findByUserUserIdAndJobJobId(userId, jobId);

        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(newStatus);

            // Save the updated status
            Application updatedApplication = applicationRepository.save(application);

            // Send an email if the status is REJECTED or SHORTLISTED
            if (newStatus == ApplicationStatus.REJECTED || newStatus == ApplicationStatus.SHORTLISTED) {
                String emailSubject = "Application Status Update for " + application.getJob().getJobTitle();
                String emailBody = generateEmailBody(application, newStatus);

                try {
                    emailService.sendEmail(application.getUser().getEmail(), emailSubject, emailBody);
                } catch (MessagingException e) {
                    throw new RuntimeException("Failed to send email", e);
                }
            }

            return updatedApplication;
        } else {
            throw new RuntimeException("Application not found for the given userId and jobId");
        }
    }

    private String generateEmailBody(Application application, ApplicationStatus status) {
        String userName = application.getUser().getFirstName();
        String jobTitle = application.getJob().getJobTitle();
        String companyName = application.getJob().getCompanyName();

        String emailBody = String.format(
                "Dear %s,\n\n"
                        + "We are writing to inform you that the status of your application for the position of %s at %s has been updated to %s.\n\n",
                userName, jobTitle, companyName, status
        );

        // Add professional message based on the status
        if (status == ApplicationStatus.REJECTED) {
            emailBody +=
                    "After careful consideration, we regret to inform you that we have decided to move forward with other candidates.\n"
                            + "We appreciate the time and effort you took to apply for this position, and we encourage you to apply for other opportunities in the future.\n\n";
        } else if (status == ApplicationStatus.SHORTLISTED) {
            emailBody +=
                    "We are pleased to inform you that your application has been shortlisted for the next phase of the hiring process.\n"
                            + "Our team will contact you shortly with further details.\n\n";
        }

        emailBody += "Thank you for your interest in the role and in joining " + companyName + ".\n\n"
                + "Best regards,\n"
                + companyName + " Hiring Team";

        return emailBody;
    }
}

