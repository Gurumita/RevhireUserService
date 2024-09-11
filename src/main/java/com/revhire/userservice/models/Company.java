//package com.revhire.userservice.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "company")
//public class Company {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "companyid")
//    private long companyId;
//
//    @Column(name = "company_name", nullable = false, length = 255)
//    private String companyName;
//
//    @Email(regexp = "[a-zA-Z0-9+\\.\\-]+@[a-zA-Z0-9\\.\\-]+\\.[a-z]{2,}", message = "invalid email ")
//    @Column(name = "email_address", nullable = false, length = 255,unique = true)
//    private String emailAddress;
//
//    @Pattern(regexp = "^[6-9]\d{9}$", message = "Phone number must be valid")
//    @Column(name = "contact_number", length = 255)
//    private String contactNumber;
//
//    @Column(name = "company_address", length = 255)
//    private String companyAddress;
//
//    @Column(name = "created_at", updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Timestamp createdAt;
//
//    @Column(name = "modified_at")
//    @Temporal(TemporalType.TIMESTAMP)
////    private Timestamp modifiedAt;
//
////    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
////    @JsonIgnore
////    private List<Employee> employees;
//}