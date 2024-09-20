package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Services.EmployerService;
import com.revhire.userservice.models.Employer;
import com.revhire.userservice.utilities.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class EmployerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployerService employerService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerUser_ShouldReturnCreated() throws Exception {
        Employer employer = new Employer();
        employer.setEmployerName("Test Employer");
        employer.setEmail("test@example.com");
        employer.setPassword("password123");
        employer.setFirstName("John");
        employer.setLastName("Doe");

        Mockito.when(employerService.createUser(any(Employer.class))).thenReturn(employer);

        mockMvc.perform(post("/api/employers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.employerName").value("Test Employer"));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        Employer employer = new Employer();
        employer.setEmployerName("Test Employer");
        employer.setEmail("test@example.com");
        employer.setFirstName("John");
        employer.setLastName("Doe");

        Mockito.when(employerService.loginUser(eq("test@example.com"), eq("password123")))
                .thenReturn(employer);

        mockMvc.perform(post("/api/employers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }



    @Test
    void resetPasswordUsingOtp_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(employerService)
                .resetPasswordUsingOtp(eq("test@example.com"), eq("123456"), eq("newPassword123"));

        mockMvc.perform(post("/api/employers/reset-password-otp")
                        .param("email", "test@example.com")
                        .param("otp", "123456")
                        .param("newPassword", "newPassword123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages").value("Password reset successfully"));
    }

    @Test
    void updateEmployer_ShouldReturnOk() throws Exception {
        Employer employer = new Employer();
        employer.setFirstName("John");
        employer.setLastName("Doe");

        Mockito.when(employerService.updateEmployer(eq(1L), any(Employer.class))).thenReturn(employer);

        mockMvc.perform(put("/api/employers/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void deleteEmployer_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(employerService).deleteEmployer(1L);

        mockMvc.perform(delete("/api/employers/delete/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void getEmployerById_ShouldReturnOk() throws Exception {
        Employer employer = new Employer();
        employer.setFirstName("John");
        employer.setLastName("Doe");

        Mockito.when(employerService.fetchEmployerById(1L)).thenReturn(employer);

        mockMvc.perform(get("/api/employers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getAllEmployers_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/employers/all"))
                .andExpect(status().isOk());
    }
}
