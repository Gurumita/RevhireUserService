package com.revhire.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.revhire.userservice.enums.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private long employeeId;

    @NotBlank(message = "User name is required")
    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "Password must meet criteria")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email ")
    @Column(name = "email", nullable = false, length = 255,unique = true)
    private String email;

    @NotNull(message = "first name should not be empty")
    @NotBlank(message = "first name cannot be blank")
    @Column(name = "first_name", length = 255)
    private String firstName;

    @NotNull(message = "last name should not be empty")
    @NotBlank(message = "last name cannot be blank")
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be valid")
    @Column(name = "contact_number", length = 255)
    private String contactNumber;

    @Column(name = "employee_address", length = 255)
    private String employeeAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "employeeRole", nullable = false)
    private EmployeeRole employeeRole;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp modifiedAt;

//    @ManyToOne
//    @JoinColumn(name = "company_id")
//    private Company company;
//
//    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Skills> skills;
}