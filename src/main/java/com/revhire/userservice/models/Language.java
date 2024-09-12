package com.revhire.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private long languageId;

    @Column(name = "language_name", nullable = false, length = 255)
    private String languageName;

    @Column(name = "proficiency", nullable = false, length = 50)
    private String proficiency;  // e.g., "Beginner", "Intermediate", "Advanced"

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
