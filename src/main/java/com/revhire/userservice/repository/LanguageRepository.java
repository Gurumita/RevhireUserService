package com.revhire.userservice.repository;

import com.revhire.userservice.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByUser_UserId(Long userId);
}
