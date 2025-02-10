package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository extends AbstractRepositoryImpl<Category> {
    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }
}
