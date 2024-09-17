package com.revhire.userservice.repository;


import com.revhire.userservice.enums.ExperienceRequired;
import com.revhire.userservice.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE " +
            "(:jobTitle IS NULL OR j.jobTitle LIKE %:jobTitle%) AND " +
            "(:location IS NULL OR j.location LIKE %:location%) AND " +
            "(:experienceRequired IS NULL OR j.experienceRequired = :experienceRequired) AND " +
            "(:skillsRequired IS NULL OR j.skillsRequired LIKE %:skillsRequired%) AND " +
            "(:companyName IS NULL OR j.companyName LIKE %:companyName%)")
    List<Job> findJobs(
            @Param("jobTitle") String jobTitle,
            @Param("location") String location,
            @Param("experienceRequired") ExperienceRequired experienceRequired,
            @Param("skillsRequired") String skillsRequired,
            @Param("companyName") String companyName);

    List<Job> findByEmployer_EmpolyerId(Long employerId);

    @Query("SELECT j FROM Job j WHERE :userId NOT IN (SELECT u.userId FROM j.applicants u)")
    List<Job> findJobsNotAppliedByUser(@Param("userId") Long userId);


}
