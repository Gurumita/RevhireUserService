package com.revhire.userservice.Services;

import com.revhire.userservice.exceptions.EmployerNotFoundException;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.exceptions.InvalidEmailException;
import com.revhire.userservice.models.Employer;
import com.revhire.userservice.repository.EmployerRepository;
import com.revhire.userservice.utilities.EmailService;
import com.revhire.userservice.utilities.PasswordEncrypter;
import com.revhire.userservice.utilities.PasswordUtils;
import com.revhire.userservice.utilities.RandomCredentialsGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EmployerService {

    private final EmailService emailService;
    private final EmployerRepository employerRepository;
    private final PasswordEncrypter passwordEncrypter;
    private final RandomCredentialsGenerator generator;

    @Autowired
    public EmployerService(EmailService emailService,
                           EmployerRepository employerRepository,
                           PasswordEncrypter passwordEncrypter,
                           RandomCredentialsGenerator generator) {
        this.emailService = emailService;
        this.employerRepository = employerRepository;
        this.passwordEncrypter = passwordEncrypter;
        this.generator = generator;
    }

    public Employer createUser(Employer employer) throws InvalidEmailException, InvalidCredentialsException {
        Employer dbUser = employerRepository.findByEmail(employer.getEmail());

        if (dbUser == null) {
            String password = employer.getPassword();
            employer.setPassword(passwordEncrypter.hashPassword(password));
            Employer createdUser = employerRepository.save(employer);

            try {
                sendWelcomeEmail(employer.getEmail(), employer.getFirstName());
            } catch (MessagingException e) {
                throw new InvalidEmailException("Failed to send email to " + employer.getEmail());
            }

            return createdUser;
        } else {
            throw new InvalidCredentialsException("Email already exists");
        }
    }

    private void sendWelcomeEmail(String email, String firstName) throws MessagingException {
        String subject = "Welcome to Rev Hire!";
        String body = "Hello " + firstName + ",\n\nYour account has been created successfully.";
        emailService.sendEmail(email, subject, body);
    }

    public Employer loginUser(String email, String password) throws InvalidCredentialsException {
        Employer dbUser = employerRepository.findByEmail(email);
        if (dbUser == null) {
            throw new InvalidCredentialsException("Invalid email");
        }

        String hashedPassword = passwordEncrypter.hashPassword(password);
        if (hashedPassword.equals(dbUser.getPassword())) {
            dbUser.setPassword(null);  // Hide password
            return dbUser;
        }

        throw new InvalidCredentialsException("Invalid password");
    }

    public String generateOtp(String email) throws InvalidCredentialsException, InvalidEmailException {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }

        String otp = generator.generateOtp();
        employer.setOtp(otp);
        employer.setOtpExpiry(Instant.now().plusSeconds(300));
        employerRepository.save(employer);

        String emailBody = "Hello " + employer.getFirstName() + ",\n\n" +
                "Use the following OTP to reset your password: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "Best regards,\n" +
                "The RevHire Team";

        try {
            emailService.sendEmail(employer.getEmail(), "Password Reset", emailBody);
        } catch (MessagingException e) {
            throw new InvalidEmailException("Failed to send email to " + employer.getEmail());
        }

        return otp;
    }

    public boolean validateOtp(String email, String otp) throws InvalidCredentialsException {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }

        return otp.equals(employer.getOtp()) && employer.getOtpExpiry().isAfter(Instant.now());
    }

    public void resetPasswordUsingOtp(String email, String otp, String newPassword) throws InvalidCredentialsException {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }

        if (employer.getOtp() == null || !employer.getOtp().equals(otp)) {
            throw new InvalidCredentialsException("Invalid OTP");
        }

        if (Instant.now().isAfter(employer.getOtpExpiry())) {
            // OTP has expired
            employer.setOtp(null);
            employer.setOtpExpiry(null);
            employerRepository.save(employer);
            throw new InvalidCredentialsException("OTP has expired");
        }

        String hashedPassword = passwordEncrypter.hashPassword(newPassword);
        employer.setPassword(hashedPassword);
        employer.setOtp(null);
        employer.setOtpExpiry(null);
        employerRepository.save(employer);

        String emailBody = "Hello " + employer.getFirstName() + ",\n\n" +
                "Your password has been successfully reset.\n\n" +
                "If you did not request this change, please contact support immediately.\n\n" +
                "Best regards,\n" +
                "The RevHire Team";

        try {
            emailService.sendEmail(employer.getEmail(), "Password Reset Successful", emailBody);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email notification to " + employer.getEmail(), e);
        }
    }

    public void updateUserPassword(String email, String oldPassword, String newPassword) throws InvalidCredentialsException {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }

        if (!passwordEncrypter.checkPassword(oldPassword, employer.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        employer.setPassword(passwordEncrypter.hashPassword(newPassword));
        employerRepository.save(employer);

        String emailBody = "Hello " + employer.getFirstName() + ",\n\n" +
                "Your password has been successfully reset.\n\n" +
                "If you did not request this change, please contact support immediately.\n\n" +
                "Best regards,\n" +
                "The RevHire Team";

        try {
            emailService.sendEmail(employer.getEmail(), "Password Reset Successful", emailBody);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email notification to " + employer.getEmail(), e);
        }
    }

    public Employer updateEmployer(Long id, Employer employerDetails) throws EmployerNotFoundException {
        Employer existingEmployer = employerRepository.findById(id)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id: " + id));

        // Update fields if they are not null
        if (employerDetails.getFirstName() != null) {
            existingEmployer.setFirstName(employerDetails.getFirstName());
        }
        if (employerDetails.getLastName() != null) {
            existingEmployer.setLastName(employerDetails.getLastName());
        }
        if (employerDetails.getEmail() != null) {
            existingEmployer.setEmail(employerDetails.getEmail());
        }
        if (employerDetails.getContactNumber() != null) {
            existingEmployer.setContactNumber(employerDetails.getContactNumber());
        }
        if (employerDetails.getAddress() != null) {
            existingEmployer.setAddress(employerDetails.getAddress());
        }
        if (employerDetails.getPassword() != null) {
            existingEmployer.setPassword(PasswordUtils.hashPassword(employerDetails.getPassword()));
        }

        // Save updated employer details
        return employerRepository.save(existingEmployer);
    }

    public void deleteEmployer(Long id) throws EmployerNotFoundException {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id: " + id));
        employerRepository.delete(employer);
    }

    public Employer fetchEmployerById(Long id) throws EmployerNotFoundException {
        return employerRepository.findById(id)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with id: " + id));
    }

    public List<Employer> fetchAllEmployers() {
        return employerRepository.findAll();
    }

    // Uncomment and update if needed
    // public Employer login(String email, String password) {
    //     Employer employer = employerRepository.findByEmail(email)
    //             .orElseThrow(() -> new EmployerNotFoundException("Employer not found with email: " + email));
    //
    //     System.out.println("Employer found with email: " + email);
    //     if (!PasswordUtils.verifyPassword(password, employer.getPassword())) {
    //         throw new IllegalArgumentException("Invalid password");
    //     }
    //
    //     return employer;
    // }
}
