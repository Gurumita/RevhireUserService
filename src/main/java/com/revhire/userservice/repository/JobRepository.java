package com.revhire.userservice.repository;

import com.revhire.userservice.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployer_EmpolyerId(Long employerId);

    @Query("SELECT j FROM Job j WHERE :userId NOT IN (SELECT u.userId FROM j.applicants u)")
    List<Job> findJobsNotAppliedByUser(@Param("userId") Long userId);


}
