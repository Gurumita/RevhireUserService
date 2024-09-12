package com.revhire.userservice.Controllers;
import com.revhire.userservice.Services.UserService;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.models.User;
import com.revhire.userservice.utilities.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<User>> registerUser(@RequestBody User user) {
        BaseResponse<User> baseResponse = new BaseResponse<>();
        System.out.println("Received password: " + user.getPassword());
        try {
            User createdUser = userService.createUser(user);
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
    public ResponseEntity<BaseResponse<User>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        BaseResponse<User> baseResponse = new BaseResponse<>();

        try {
            User user = userService.loginUser(email, password);
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
    @GetMapping("/otp")
    public ResponseEntity<BaseResponse<String>> generateOTP(String email) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setData(userService.generateOtp(email));
        baseResponse.setStatus(HttpStatus.CREATED.value());
        baseResponse.setMessages("OTP generated");
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }
}
