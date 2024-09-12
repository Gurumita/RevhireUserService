package com.revhire.userservice.repository;


import com.revhire.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface UserRepository extends JpaRepository<  User, Long> {
    User findByUserName(String userName);
    User findByEmail(String email);
}
