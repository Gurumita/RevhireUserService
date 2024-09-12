package com.revhire.userservice.repository;


import com.revhire.userservice.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

}

