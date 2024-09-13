package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.CustomUserDetailsService;
import com.revhire.userservice.Services.UserService;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.jwt.JwtUtil;
import com.revhire.userservice.models.AuthResponse;
import com.revhire.userservice.models.User;
import com.revhire.userservice.utilities.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthResponse>> registerUser(@RequestBody User user) {
        BaseResponse<AuthResponse> baseResponse = new BaseResponse<>();
        try {
            User createdUser = userService.createUser(user);

            // Authenticate and generate JWT token
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(createdUser.getUserName(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(createdUser.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            baseResponse.setMessages("Registration Successful");
            baseResponse.setData(new AuthResponse(jwt));
            baseResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessages(e.getMessage());
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during registration: " + e.getMessage());
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        BaseResponse<AuthResponse> baseResponse = new BaseResponse<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("Login Successful");
            baseResponse.setData(new AuthResponse(jwt));
            return ResponseEntity.ok(baseResponse);
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponse.setMessages(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse<String>> forgotPassword(@RequestParam String email) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        try {
            userService.generateOtp(email);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("OTP sent to email");
            baseResponse.setData("Check your email for the OTP.");
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessages(e.getMessage());
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during OTP generation: " + e.getMessage());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/reset-password-otp")
    public ResponseEntity<BaseResponse<String>> resetPasswordUsingOtp(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        try {
            userService.resetPasswordUsingOtp(email, otp, newPassword);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("Password reset successfully");
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessages(e.getMessage());
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during password reset: " + e.getMessage());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        try {
            userService.updateUserPassword(email, oldPassword, newPassword);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("Password updated successfully");
        } catch (InvalidCredentialsException e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessages(e.getMessage());
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessages("Error during password update: " + e.getMessage());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Fetching user details for: {}", userDetails != null ? userDetails.getUsername() : "null");
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        return ResponseEntity.ok(userDetails);
    }
}
