package com.revhire.userservice.MockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.userservice.Controllers.LanguageController;
import com.revhire.userservice.models.Language;
import com.revhire.userservice.Services.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguageController languageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(languageController).build();
    }

    @Test
    void testCreateLanguage() throws Exception {
        Language language = new Language(1L, "English", "Advanced", null);
        when(languageService.createLanguage(any(Language.class))).thenReturn(language);

        mockMvc.perform(post("/api/languages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(language)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.languageName").value("English"));
    }

    @Test
    void testGetAllLanguages() throws Exception {
        Language language1 = new Language(1L, "English", "Advanced", null);
        Language language2 = new Language(2L, "Spanish", "Intermediate", null);
        List<Language> languages = Arrays.asList(language1, language2);

        when(languageService.getAllLanguages()).thenReturn(languages);

        mockMvc.perform(get("/api/languages/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetLanguagesByUserId() throws Exception {
        Language language1 = new Language(1L, "English", "Advanced", null);
        Language language2 = new Language(2L, "Spanish", "Intermediate", null);
        List<Language> languages = Arrays.asList(language1, language2);

        when(languageService.getLanguagesByUserId(1L)).thenReturn(languages);

        mockMvc.perform(get("/api/languages/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetLanguagesByUserIdNotFound() throws Exception {
        when(languageService.getLanguagesByUserId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/languages/user/1"))
                .andExpect(status().isNotFound());
    }
}
