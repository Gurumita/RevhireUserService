package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.UserService;
import com.revhire.userservice.enums.Role;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.exceptions.InvalidEmailException;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.UserRepository;
import com.revhire.userservice.utilities.EmailService;
import com.revhire.userservice.utilities.PasswordEncrypter;
import com.revhire.userservice.utilities.RandomCredentialsGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.revhire.userservice.exceptions.NotFoundException; // Add this import

import jakarta.mail.MessagingException;

import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncrypter passwordEncrypter;

    @Mock
    private RandomCredentialsGenerator generator;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUser_WhenEmailDoesNotExist() throws MessagingException, InvalidCredentialsException, InvalidEmailException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(passwordEncrypter.hashPassword(any(String.class))).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(user);

        verify(userRepository).save(user);
        verify(emailService).sendEmail(eq("test@example.com"), any(String.class), any(String.class));
    }





    @Test
    void loginUser_ShouldReturnUser_WhenCredentialsAreValid() throws InvalidCredentialsException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        when(passwordEncrypter.hashPassword(any(String.class))).thenReturn("hashedPassword");

        User result = userService.loginUser("test@example.com", "password");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }



    @Test
    void generateOtp_ShouldGenerateOtp_WhenEmailExists() throws MessagingException, InvalidCredentialsException, InvalidEmailException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        when(generator.generateOtp()).thenReturn("123456");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String otp = userService.generateOtp("test@example.com");

        assertEquals("123456", otp);
        verify(emailService).sendEmail(eq("test@example.com"), any(String.class), any(String.class));
    }


    @Test
    void validateOtp_ShouldReturnTrue_WhenOtpIsValid() throws InvalidCredentialsException {
        User user = new User();
        user.setOtp("123456");
        user.setOtpExpiry(Instant.now().plusSeconds(60));

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        boolean isValid = userService.validateOtp("test@example.com", "123456");

        assertTrue(isValid);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenOtpIsInvalid() throws InvalidCredentialsException {
        User user = new User();
        user.setOtp("123456");
        user.setOtpExpiry(Instant.now().plusSeconds(60));

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        boolean isValid = userService.validateOtp("test@example.com", "wrongOtp");

        assertFalse(isValid);
    }

    @Test
    void resetPasswordUsingOtp_ShouldResetPassword_WhenOtpIsValid() throws MessagingException, InvalidCredentialsException {
        User user = new User();
        user.setOtp("123456");
        user.setOtpExpiry(Instant.now().plusSeconds(60));
        user.setEmail("test@example.com");
        user.setFirstName("John");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        when(passwordEncrypter.hashPassword(any(String.class))).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.resetPasswordUsingOtp("test@example.com", "123456", "newPassword");

        verify(emailService).sendEmail(eq("test@example.com"), any(String.class), any(String.class));
    }




    @Test
    void updateUserPassword_ShouldUpdatePassword_WhenOldPasswordIsValid() throws MessagingException, InvalidCredentialsException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedOldPassword");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        when(passwordEncrypter.checkPassword(any(String.class), any(String.class))).thenReturn(true);
        when(passwordEncrypter.hashPassword(any(String.class))).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUserPassword("test@example.com", "oldPassword", "newPassword");

        verify(emailService).sendEmail(eq("test@example.com"), any(String.class), any(String.class));
    }



    @Test
    void fetchByEmail_ShouldReturnUser_WhenUserExists() throws NotFoundException {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        User result = userService.fetchByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void fetchByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.fetchByEmail("test@example.com");
        });

        assertEquals("User with email: test@example.com not found", exception.getMessage());
    }
}
