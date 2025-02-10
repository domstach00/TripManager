package com.example.tripmanager.budget.service;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return this.categoryRepository.save(category);
    }
}
