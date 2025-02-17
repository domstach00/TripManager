package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.mapper.CategoryMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;
import com.example.tripmanager.budget.repository.BudgetRepository;
import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.shared.model.AbstractEntity;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryService categoryService;

    public Budget createBudgetFromTemplate(BudgetTemplate budgetTemplate) {
        Budget budget = BudgetMapper.budgetFromTemplate(budgetTemplate);
        return budgetRepository.save(budget);
    }

    public Budget createBudget(BudgetCreateForm budgetCreateForm, Account currentAccount) {
        Budget budgetToCreate = BudgetMapper.budgetFromCreateForm(budgetCreateForm, currentAccount);
        Budget savedBudget = budgetRepository.save(budgetToCreate);

        // TODO: Invite members to created budget

        return savedBudget;
    }

    public Page<Budget> getBudgetsRelatedToAccount(Pageable pageable, Account account, boolean includeArchived) {
        return budgetRepository.getBudgetRelatedWhereGivenAccountIsMember(pageable, account, includeArchived);
    }

    public Budget getBudgetById(String budgetId, Account currentAccount) {
        if (StringUtils.isBlank(budgetId)) {
            throw new IllegalArgumentException("Budget id is empty");
        }
        return budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
    }

    public Budget archiveBudget(String budgetId, Account currentAccount) {
        Budget budgetToArchive = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
        if (budgetToArchive.isArchived()) {
            throw new IllegalStateException("Budget is already archived");
        }

        budgetToArchive.setArchived(true);

        return budgetRepository.save(budgetToArchive);
    }

    public Budget unArchiveBudget(String budgetId, Account currentAccount) {
        Budget budgetToArchive = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
        if (!budgetToArchive.isArchived()) {
            throw new IllegalStateException("Budget is already not archived");
        }

        budgetToArchive.setArchived(false);

        return budgetRepository.save(budgetToArchive);
    }

    public void deleteBudget(String budgetId, Account currentAccount) {
        Budget budgetToDelete = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
        if (!budgetToDelete.isDeleted()) {
            budgetToDelete.setDeleted(true);
        }
        budgetRepository.save(budgetToDelete);
    }

    public void leaveBudget(String budgetId, Account currentAccount) {
        if (budgetId == null || currentAccount == null) {
            throw new IllegalArgumentException("Budget ID and current account cannot be null");
        }
        Budget budgetToLeave = budgetRepository.getBudgetByIdWhereAccountIsMember(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));

        ObjectId currentAccountId = AbstractEntity.toObjectId(currentAccount.getId());
        List<ObjectId> updatedMembers = budgetToLeave.getMembers().stream()
                .filter(memberId -> !memberId.equals(currentAccountId))
                .collect(Collectors.toList());

        if (updatedMembers.size() == budgetToLeave.getMembers().size()) {
            return;
        }

        budgetToLeave.setMembers(updatedMembers);
        budgetRepository.save(budgetToLeave);
    }

    @Transactional
    public Budget editBudget(String budgetId, BudgetCreateForm editedBudget, Account currentAccount) {
        if (budgetId == null || editedBudget == null || currentAccount == null) {
            throw new IllegalArgumentException("Updated Budget, Id and current account cannot be null");
        }
        Budget orginalBudget = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));

        orginalBudget.setName(editedBudget.getName());
        orginalBudget.setDescription(editedBudget.getDescription());
        orginalBudget.setAllocatedBudget(editedBudget.getAllocatedBudget());
        if (editedBudget.getStartDate() != null) {
            orginalBudget.setStartDate(editedBudget.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (editedBudget.getEndDate() != null) {
            orginalBudget.setEndDate(editedBudget.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        // TODO: Invite members to edited budget

        return budgetRepository.save(orginalBudget);
    }

    @Transactional
    public Budget addCategoryToBudget(String budgetId, CategoryCreateForm categoryCreateForm, Account currentAccount) {
        if (budgetId == null || categoryCreateForm == null || currentAccount == null) {
            throw new IllegalArgumentException("Category form, BudgetId and current account cannot be null");
        }

        Budget budget = budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
        boolean doesSameNameExist = budget.getCategories().stream()
                .anyMatch(category -> StringUtils.equalsIgnoreCase(category.getName(), categoryCreateForm.getName()));
        if (doesSameNameExist) {
            throw new IllegalArgumentException("Same category name (\""+ categoryCreateForm.getName() +"\") already exists in same Budget");
        }
        Category categoryToSave = CategoryMapper.categoryFromCreateForm(categoryCreateForm);
        categoryService.createCategory(categoryToSave);

        budget.addCategory(categoryToSave);
        return budgetRepository.save(budget);
    }

    @Transactional
    public SubCategory addSubCategoryToBudget(String budgetId, String categoryId, SubCategoryCreateForm subCategoryCreateForm, Account currentAccount) {
        if (budgetId == null || categoryId == null || subCategoryCreateForm == null || currentAccount == null) {
            throw new IllegalArgumentException("SubCategory form, BudgetId, CategoryId and current account cannot be null");
        }

        Budget budget = budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Budget was not found or you do not have enough permissions"));
        boolean isCategoryInBudget = budget.getCategories().stream().anyMatch(category -> Objects.equals(category.getId(), categoryId));
        if (!isCategoryInBudget) {
            throw new IllegalArgumentException("Budget does not contains this category");
        }
        Category category = categoryService.getCategory(categoryId)
                .orElseThrow(() -> new ItemNotFound("Category was not found or you do not have enough permissions"));
        SubCategory subCategoryToSave = CategoryMapper.subCategoryFromCreateForm(subCategoryCreateForm);
        category.addSubCategory(subCategoryToSave);
        categoryService.saveCategory(category);
        return subCategoryToSave;
    }
}
