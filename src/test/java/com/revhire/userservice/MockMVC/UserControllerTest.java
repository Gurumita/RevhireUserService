package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.UserController;
import com.revhire.userservice.Services.UserService;
import com.revhire.userservice.dto.AuthRequest;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.models.User;
import com.revhire.userservice.utilities.BaseResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setUserName("JohnDoe");
        user.setPassword("password");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.messages").value("Registration Successful"))
                .andExpect(jsonPath("$.data.userName").value("JohnDoe"));
    }



    @Test
    public void testLogin_Success() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("john@example.com");
        authRequest.setPassword("password");

        User user = new User();
        user.setUserId(1L);
        user.setEmail("john@example.com");

        when(userService.loginUser(any(AuthRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages").value("Login Successful"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }



    @Test
    public void testForgotPassword_Success() throws Exception {
        String email = "john@example.com";

        mockMvc.perform(post("/api/users/forgot-password")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages").value("OTP sent to email"));
    }

    @Test
    public void testResetPasswordUsingOtp_Success() throws Exception {
        String email = "john@example.com";
        String otp = "123456";
        String newPassword = "newPassword";

        mockMvc.perform(post("/api/users/reset-password-otp")
                        .param("email", email)
                        .param("otp", otp)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages").value("Password reset successfully"));
    }
}
