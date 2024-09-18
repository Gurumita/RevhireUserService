package com.revhire.userservice.models;


import com.revhire.userservice.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @NotBlank(message = "User name is required")
    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;


    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "Password must meet criteria")
    @Column(name = "password", nullable = false, length = 255)
    private String password;


    @Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "Invalid email")
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @NotNull(message = "First name should not be empty")
    @NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name", length = 255)
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be valid")
    @Column(name = "contact_number", length = 255)
    private String contactNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Skills> skills = new ArrayList<>();


    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.modifiedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = Instant.now();
    }
    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    private Instant otpExpiry;

}