package com.revhire.userservice.Mockito;

import com.revhire.userservice.Services.LanguageService;
import com.revhire.userservice.models.Language;
import com.revhire.userservice.models.User;
import com.revhire.userservice.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLanguage() {
        Language language = new Language();
        language.setLanguageId(1L);
        language.setLanguageName("English");
        language.setProficiency("Advanced");

        when(languageRepository.save(any(Language.class))).thenReturn(language);

        Language createdLanguage = languageService.createLanguage(language);

        assertNotNull(createdLanguage);
        assertEquals("English", createdLanguage.getLanguageName());
        assertEquals("Advanced", createdLanguage.getProficiency());
        verify(languageRepository, times(1)).save(language);
    }

    @Test
    void getAllLanguages() {
        Language language1 = new Language();
        Language language2 = new Language();
        List<Language> languages = Arrays.asList(language1, language2);

        when(languageRepository.findAll()).thenReturn(languages);

        List<Language> result = languageService.getAllLanguages();

        assertEquals(2, result.size());
        verify(languageRepository, times(1)).findAll();
    }

    @Test
    void getLanguagesByUserId() {
        Language language1 = new Language();
        Language language2 = new Language();
        User user = new User();
        user.setUserId(1L);
        language1.setUser(user);
        language2.setUser(user);
        List<Language> languages = Arrays.asList(language1, language2);

        when(languageRepository.findAll()).thenReturn(languages);

        List<Language> result = languageService.getLanguagesByUserId(1L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(lang -> lang.getUser().getUserId() == 1L));
        verify(languageRepository, times(1)).findAll();
    }
}
