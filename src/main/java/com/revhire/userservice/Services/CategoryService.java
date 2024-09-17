package com.revhire.userservice.Services;

import com.revhire.userservice.models.Category;
import com.revhire.userservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    // Create a new Category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Update an existing Category by ID
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        category.setCategoryName(categoryDetails.getCategoryName());
        category.setCategoryDescription(categoryDetails.getCategoryDescription());

        return categoryRepository.save(category);
    }

    // Delete a Category by ID
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    // Get all categories (optional, if needed)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get a single category by ID (optional, if needed)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

}
