package com.revhire.userservice.Controllers;

import com.revhire.userservice.Services.LanguageService;
import com.revhire.userservice.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @PostMapping("/create")
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) {
        Language createdLanguage = languageService.createLanguage(language);
        return new ResponseEntity<>(createdLanguage, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Language>> getLanguagesByUserId(@PathVariable Long userId) {
        List<Language> languages = languageService.getLanguagesByUserId(userId);
        if (!languages.isEmpty()) {
            return new ResponseEntity<>(languages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
