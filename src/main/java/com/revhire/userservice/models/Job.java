package com.revhire.userservice.models;



import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.enums.JobType;
import com.revhire.userservice.enums.SalaryRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_description", columnDefinition = "TEXT", nullable = false)
    private String jobDescription;

    @Column(name = "skills_required", nullable = false)
    private String skillsRequired;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_range", nullable = false)
    private SalaryRange salaryRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_required", nullable = false)
    private ExperienceRequired experienceRequired;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @Column(name = "post_date")
    private Date postDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "job_applicants",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> applicants = new HashSet<>();

    public Set<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(Set<User> applicants) {
        this.applicants = applicants;
    }
}