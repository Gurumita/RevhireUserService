package com.revhire.userservice.Mockito;

import com.revhire.userservice.Controllers.CategoryController;
import com.revhire.userservice.Services.CategoryService;
import com.revhire.userservice.models.Category;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    public CategoryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCategory() {
        Category mockCategory = new Category();
        when(categoryService.createCategory(mockCategory)).thenReturn(mockCategory);

        ResponseEntity<Category> response = categoryController.createCategory(mockCategory);

        assertEquals(ResponseEntity.ok(mockCategory), response);
        verify(categoryService, times(1)).createCategory(mockCategory);
    }

    @Test
    public void testGetAllCategories() {
        List<Category> mockCategories = Arrays.asList(new Category(), new Category());
        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(ResponseEntity.ok(mockCategories), response);
        verify(categoryService, times(1)).getAllCategories();
    }
}


