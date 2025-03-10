package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.repository.CategoryRepository;
import com.example.tripmanager.shared.exception.ItemNotFound;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
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

    /**
     * Before using this method, make sure to un-assign this category from transactions.
     */
    public void deleteCategory(Account currentAccount, String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            log.error("Attempt to delete category with null or blank id, by accountId={}", currentAccount.getId());
            throw new IllegalArgumentException("Category id cannot be empty");
        }
        log.debug("AccountId={} is trying to delete categoryId={}", currentAccount.getId(), categoryId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            log.warn("Category with id={} was not found", categoryId);
            throw new ItemNotFound("Category with id={} was not found");
        }
        Category categoryToSave = categoryOpt.get();
        categoryToSave.setDeleted(true);
        categoryRepository.save(categoryToSave);
        log.info("Category with Id={} marked as deleted", categoryToSave.getId());
    }
}
