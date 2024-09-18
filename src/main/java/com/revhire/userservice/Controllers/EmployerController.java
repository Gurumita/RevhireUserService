package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.EmployerService;
import com.revhire.userservice.exceptions.EmployerNotFoundException;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.models.Employer;
import com.revhire.userservice.utilities.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Employer>> registerUser(@RequestBody Employer user) {
        BaseResponse<Employer> baseResponse = new BaseResponse<>();
        System.out.println("Received password: " + user.getPassword());
        try {
            Employer createdUser = employerService.createUser(user);
            baseResponse.setMessages("Registration Successful");
            baseResponse.setData(createdUser);
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
    public ResponseEntity<BaseResponse<Employer>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        BaseResponse<Employer> baseResponse = new BaseResponse<>();

        try {
            Employer user = employerService.loginUser(email, password);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessages("Login Successful");
            baseResponse.setData(user);
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
            employerService.generateOtp(email);
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
            employerService.resetPasswordUsingOtp(email, otp, newPassword);
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
            employerService.updateUserPassword(email, oldPassword, newPassword);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Employer> updateEmployer(@PathVariable Long id, @RequestBody Employer employerDetails) throws EmployerNotFoundException {
        Employer updatedEmployer = employerService.updateEmployer(id, employerDetails);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long id) throws EmployerNotFoundException {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getEmployerById(@PathVariable Long id) throws EmployerNotFoundException {
        Employer employer = employerService.fetchEmployerById(id);
        return ResponseEntity.ok(employer);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employer>> getAllEmployers() {
        List<Employer> employers = employerService.fetchAllEmployers();
        return ResponseEntity.ok(employers);
    }




}
