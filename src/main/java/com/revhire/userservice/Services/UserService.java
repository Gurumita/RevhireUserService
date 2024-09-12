package com.revhire.userservice.Services;

import com.revhire.userservice.repository.UserRepository;
import com.revhire.userservice.enums.Role;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.exceptions.InvalidEmailException;
import com.revhire.userservice.exceptions.NotFoundException;
import com.revhire.userservice.models.User;
import com.revhire.userservice.models.Skills;
import com.revhire.userservice.utilities.EmailService;
import com.revhire.userservice.utilities.ModelUpdater;
import com.revhire.userservice.utilities.PasswordEncrypter;
import com.revhire.userservice.utilities.RandomCredentialsGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SkillsService skillsService;

    @Autowired
    private PasswordEncrypter passwordEncrypter;

    @Autowired
    private RandomCredentialsGenerator generator;

    @Autowired
    private ModelUpdater modelUpdater;

    public User createUser(User user) {
        User dbUser = userRepository.findByEmail(user.getEmail());

        if (dbUser == null) {
//            if (user.getSkills() == null) {
//                user.setSkills(new ArrayList<>());
//            }
            if (user.getRole() == null || !isValidRole(user.getRole())) {
                throw new InvalidCredentialsException("Invalid role");
            }

            String password = user.getPassword();
            user.setPassword(passwordEncrypter.hashPassword(password));
            User createdUser = userRepository.save(user);

            try {
                sendWelcomeEmail(user.getEmail(), user.getFirstName());
            } catch (MessagingException e) {
                throw new InvalidEmailException("Failed to send email to " + user.getEmail());
            }

            return createdUser;
        } else {
            throw new InvalidCredentialsException("Email already exists");
        }
    }

    private boolean isValidRole(Role role) {
        return Arrays.asList(Role.values()).contains(role);
    }

    private void sendWelcomeEmail(String email, String firstName) throws MessagingException {
        String subject = "Welcome to Rev Hire!";
        String body = "Hello " + firstName + ",\n\nYour account has been created successfully.";
        emailService.sendEmail(email, subject, body);
    }

    public User loginUser(String email, String password) {
        User dbUser = userRepository.findByEmail(email);
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


    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public String generateOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new InvalidCredentialsException("Email does not exist");
        }

        String OTP = generator.generateOtp();
        String emailBody = "Hello " + user.getFirstName() + ",\n\n" +
                "Use the following OTP to reset your password: " + OTP + "\n\n" +
                "Best regards,\n" +
                "The RevHire Team";

        try {
            emailService.sendEmail(user.getEmail(), "Password Reset", emailBody);
        } catch (MessagingException e) {
            throw new InvalidEmailException("Failed to send email to " + user.getEmail());
        }

        return OTP;
    }

    public User fetchByEmail(String email) {
        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            throw new NotFoundException("User with email: " + email + " not found");
        }
        return dbUser;
    }

    public User updateUserPassword(String email, String password) {
        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            throw new NotFoundException("No user with the email " + email);
        }

        dbUser.setPassword(passwordEncrypter.hashPassword(password));
        return userRepository.save(dbUser);
    }
}
