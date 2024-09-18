package com.revhire.userservice.Mockito;

import com.revhire.userservice.Controllers.EmployerController;
import com.revhire.userservice.Services.EmployerService;
import com.revhire.userservice.exceptions.InvalidCredentialsException;
import com.revhire.userservice.exceptions.InvalidEmailException;
import com.revhire.userservice.models.Employer;
import com.revhire.userservice.utilities.BaseResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployerControllerTest {

    @Mock
    private EmployerService employerService;

    @InjectMocks
    private EmployerController employerController;

    public EmployerControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() throws InvalidCredentialsException, InvalidEmailException {
        Employer mockEmployer = new Employer();
        BaseResponse<Employer> mockResponse = new BaseResponse<>();
        mockResponse.setMessages("Registration Successful");
        mockResponse.setData(mockEmployer);

        when(employerService.createUser(mockEmployer)).thenReturn(mockEmployer);

        ResponseEntity<BaseResponse<Employer>> response = employerController.registerUser(mockEmployer);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockEmployer, response.getBody().getData());
    }

    @Test
    public void testRegisterUser_Failure() throws InvalidCredentialsException, InvalidEmailException {
        Employer mockEmployer = new Employer();
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        when(employerService.createUser(mockEmployer)).thenThrow(exception);

        ResponseEntity<BaseResponse<Employer>> response = employerController.registerUser(mockEmployer);

        assertEquals(400, response.getStatusCodeValue());
    }
}



