package com.example.tripmanager.budget.mapper;

import com.example.tripmanager.budget.model.BudgetType;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;

public class CategoryMapper {
    public static Category categoryFromCreateForm(CategoryCreateForm categoryCreateForm) {
        if (categoryCreateForm == null) {
            return null;
        }
        Category category = new Category();
        category.setName(categoryCreateForm.getName());
        category.setType(BudgetType.valueOf(categoryCreateForm.getType()));
        category.setAllocatedAmount(categoryCreateForm.getAllocatedAmount());
        return category;
    }

    public static SubCategory subCategoryFromCreateForm(SubCategoryCreateForm subCategoryCreateForm) {
        if (subCategoryCreateForm == null) {
            return null;
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryCreateForm.getName());
        return subCategory;
    }
}
