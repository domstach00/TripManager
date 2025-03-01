package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (category == null) {
            log.error("Attempt to create a null category");
            throw new IllegalArgumentException("Category cannot be null");
        }
        log.debug("Creating a new category: {}", category.getName());
        Category createdCategory = this.categoryRepository.save(category);
        log.info("Category successfully created with ID: {}", createdCategory.getId());
        return createdCategory;
    }

    public Optional<Category> getCategory(String categoryId) {
        if (categoryId == null) {
            log.warn("Attempted to fetch category with null ID");
            throw new IllegalArgumentException("CategoryId cannot be null");
        }
        log.debug("Fetching category with ID: {}", categoryId);
        Optional<Category> categoryOpt = this.categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            log.warn("No category found for ID: {}", categoryId);
        } else {
            log.info("Category found: {}", categoryOpt.get().getName());
        }
        return categoryOpt;
    }

    public Category saveCategory(Category category) {
        if (category == null) {
            log.error("Attempted to save a null category");
            throw new IllegalArgumentException("Category cannot be null");
        }

        log.debug("Saving category: {}", category.getName());
        Category savedCategory = categoryRepository.save(category);
        log.info("Category successfully saved with ID: {}", savedCategory.getId());

        return savedCategory;
    }
}
