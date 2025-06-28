package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.mapper.CategoryMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.model.InviteMembersRequest;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;
import com.example.tripmanager.budget.repository.BudgetRepository;
import com.example.tripmanager.email.service.EmailService;
import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.token.BudgetInvitationToken;
import com.example.tripmanager.shared.token.service.TokenService;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;

    public Budget createBudgetFromTemplate(BudgetTemplate budgetTemplate) {
        log.debug("Creating a budget from template: {}", budgetTemplate.getId());
        Budget budget = BudgetMapper.budgetFromTemplate(budgetTemplate);
        Budget createdBudget = budgetRepository.save(budget);
        log.info("Budget successfully created from template with ID: {}", createdBudget.getId());
        return createdBudget;
    }

    public Budget createBudget(BudgetCreateForm budgetCreateForm, Account currentAccount) {
        log.debug("Creating a new budget for user: {}", currentAccount.getEmail());
        Budget budgetToCreate = BudgetMapper.budgetFromCreateForm(budgetCreateForm, currentAccount);
        Budget savedBudget = budgetRepository.save(budgetToCreate);

        // TODO: Invite members to created budget
        log.info("Budget successfully created with ID: {}", savedBudget.getId());
        return savedBudget;
    }

    public Page<Budget> getBudgetsRelatedToAccount(Pageable pageable, Account account, boolean includeArchived) {
        log.debug("Fetching budgets for account: {}, includeArchived: {}", account.getEmail(), includeArchived);
        return budgetRepository.getBudgetRelatedWhereGivenAccountIsMember(pageable, account, includeArchived);
    }

    public Optional<Budget> getBudgetById(String budgetId, Account currentAccount) {
        validateBudgetId(budgetId);
        log.debug("Fetching budget with ID: {}", budgetId);
        return budgetRepository.getBudgetById(budgetId, currentAccount);
    }

    public void inviteMembersToBudget(@NotNull Account currentAccount, String budgetId, InviteMembersRequest inviteMembersRequest) {
        if (StringUtils.isBlank(budgetId)) {
            log.warn("Budget ID is blank.");
            throw new IllegalArgumentException("Budget ID must not be blank.");
        } else if (inviteMembersRequest == null) {
            log.warn("InviteMembersRequest is null.");
            throw new IllegalArgumentException("InviteMembersRequest must not be null.");
        }

        List<String> memberIds = inviteMembersRequest.getIds();
        if (memberIds == null || memberIds.isEmpty()) {
            log.warn("No member IDs provided for invitation.");
            return;
        }

        List<Account> accountsToInvite = new ArrayList<>();
        int pageNumber = 0;
        int pageSize = Math.min(memberIds.size(), 50);
        Page<Account> accountPage;

        do {
            Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
            accountPage = accountService.getAccountsByIds(pageable, memberIds);
            accountsToInvite.addAll(accountPage.getContent());
        } while (accountPage.hasNext());

        if (accountsToInvite.isEmpty()) {
            log.warn("No accounts found for the specified IDs.");
            return;
        }

        Map<String, Object> additionalData = Map.of(BudgetInvitationToken.FIELD_NAME_BUDGET_ID, budgetId);
        BudgetInvitationToken generatedToken = tokenService.generateToken(currentAccount.getId(), TokenType.BUDGET_INVITATION, additionalData);
        emailService.sendInviteEmail(currentAccount, generatedToken.getTokenValue(), accountsToInvite);
    }

    public Budget getBudgetByIdOrThrow(String budgetId, Account currentAccount) {
        validateBudgetId(budgetId);
        log.debug("Fetching budget with ID: {}", budgetId);
        return budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });
    }

    private void validateBudgetId(String budgetId) {
        if (StringUtils.isBlank(budgetId)) {
            log.warn("Attempted to fetch budget with empty ID");
            throw new IllegalArgumentException("Budget id is empty");
        }
    }

    public Budget archiveBudget(String budgetId, Account currentAccount) {
        log.debug("Archiving budget with ID: {}", budgetId);
        Budget budgetToArchive = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for archiving ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });
        if (budgetToArchive.isArchived()) {
            log.warn("Attempted to archive an already archived budget: {}", budgetId);
            throw new IllegalStateException("Budget is already archived");
        }

        budgetToArchive.setArchived(true);
        Budget updatedBudget = budgetRepository.save(budgetToArchive);
        log.info("Budget successfully archived with ID: {}", updatedBudget.getId());
        return updatedBudget;
    }

    public Budget unArchiveBudget(String budgetId, Account currentAccount) {
        log.info("Unarchiving budget with ID: {}", budgetId);
        Budget budgetToUnarchive = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for unarchiving ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });

        if (!budgetToUnarchive.isArchived()) {
            log.warn("Attempted to unarchive a non-archived budget: {}", budgetId);
            throw new IllegalStateException("Budget is already not archived");
        }

        budgetToUnarchive.setArchived(false);
        Budget unarchivedBudget = budgetRepository.save(budgetToUnarchive);
        log.info("Budget successfully unarchived with ID: {}", budgetId);
        return unarchivedBudget;
    }

    public void deleteBudget(String budgetId, Account currentAccount) {
        log.info("Deleting budget with ID: {}", budgetId);
        Budget budgetToDelete = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for deletion ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });

        if (!budgetToDelete.isDeleted()) {
            budgetToDelete.setDeleted(true);
        }

        budgetRepository.save(budgetToDelete);
        log.info("Budget successfully marked as deleted with ID: {}", budgetId);
    }

    public void leaveBudget(String budgetId, Account currentAccount) {
        if (budgetId == null || currentAccount == null) {
            log.warn("Attempted to leave a budget with null ID or account");
            throw new IllegalArgumentException("Budget ID and current account cannot be null");
        }

        log.info("User {} leaving budget with ID: {}", currentAccount.getEmail(), budgetId);
        Budget budgetToLeave = budgetRepository.getBudgetByIdWhereAccountIsMember(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for leaving ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });

        ObjectId currentAccountId = AbstractEntity.toObjectId(currentAccount.getId());
        List<ObjectId> updatedMembers = budgetToLeave.getMembers().stream()
                .filter(memberId -> !memberId.equals(currentAccountId))
                .collect(Collectors.toList());

        if (updatedMembers.size() == budgetToLeave.getMembers().size()) {
            log.info("User {} is not a member of budget ID: {}, no changes made", currentAccount.getEmail(), budgetId);
            return;
        }

        budgetToLeave.setMembers(updatedMembers);
        budgetRepository.save(budgetToLeave);
        log.info("User {} successfully left budget ID: {}", currentAccount.getEmail(), budgetId);
    }

    @Transactional
    public Budget editBudget(String budgetId, BudgetCreateForm editedBudget, Account currentAccount) {
        if (budgetId == null || editedBudget == null || currentAccount == null) {
            log.warn("Attempted to edit a budget with null parameters");
            throw new IllegalArgumentException("Updated Budget, Id and current account cannot be null");
        }
        log.debug("Editing budget with ID: {} for user: {}", budgetId, currentAccount.getEmail());
        Budget orginalBudget = budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });

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

        Budget updatedBudget = budgetRepository.save(orginalBudget);
        log.info("Budget successfully updated with ID: {}", updatedBudget.getId());
        return updatedBudget;
    }

    @Transactional
    public Budget addCategoryToBudget(String budgetId, CategoryCreateForm categoryCreateForm, Account currentAccount) {
        if (budgetId == null || categoryCreateForm == null || currentAccount == null) {
            log.warn("Attempted to add a category with null parameters");
            throw new IllegalArgumentException("Category form, BudgetId and current account cannot be null");
        }

        Budget budget = budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for adding category ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });
        boolean doesSameNameExist = budget.getCategories().stream()
                .anyMatch(category -> StringUtils.equalsIgnoreCase(category.getName(), categoryCreateForm.getName()));
        if (doesSameNameExist) {
            log.warn("Attempted to add a duplicate category '{}' to budget ID: {}", categoryCreateForm.getName(), budgetId);
            throw new IllegalArgumentException("Same category name (\""+ categoryCreateForm.getName() +"\") already exists in same Budget");
        }
        Category categoryToSave = CategoryMapper.categoryFromCreateForm(categoryCreateForm);
        Category savedCategory = categoryService.createCategory(categoryToSave);
        budget.addCategory(savedCategory);

        Budget updatedBudget = budgetRepository.save(budget);
        log.info("Category '{}' successfully added to budget ID: {}", savedCategory.getName(), budgetId);

        return updatedBudget;
    }

    @Transactional
    public SubCategory addSubCategoryToBudget(String budgetId, String categoryId, SubCategoryCreateForm subCategoryCreateForm, Account currentAccount) {
        if (budgetId == null || categoryId == null || subCategoryCreateForm == null || currentAccount == null) {
            log.warn("Attempted to add a subcategory with null parameters");
            throw new IllegalArgumentException("SubCategory form, BudgetId, CategoryId and current account cannot be null");
        }

        Budget budget = budgetRepository.getBudgetById(budgetId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Budget not found or insufficient permissions for adding subcategory ID: {}", budgetId);
                    return new ItemNotFound("Budget was not found or you do not have enough permissions");
                });
        boolean isCategoryInBudget = budget.getCategories().stream().anyMatch(category -> Objects.equals(category.getId(), categoryId));
        if (!isCategoryInBudget) {
            log.warn("Attempted to add a subcategory to a non-existing category ID: {} in budget ID: {}", categoryId, budgetId);
            throw new IllegalArgumentException("Budget does not contains this category");
        }
        Category category = categoryService.getCategory(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with ID: {}", categoryId);
                    return new ItemNotFound("Category was not found or you do not have enough permissions");
                });
        SubCategory subCategoryToSave = CategoryMapper.subCategoryFromCreateForm(subCategoryCreateForm);
        category.addSubCategory(subCategoryToSave);
        categoryService.saveCategory(category);
        log.info("Subcategory '{}' successfully added to category ID: {} in budget ID: {}", subCategoryToSave.getName(), categoryId, budgetId);
        return subCategoryToSave;
    }

    public List<Category> getCategoriesForBudget(Account currentAccount, String budgetId) {
        log.debug("Fetching categories for budget ID: {}", budgetId);
        Budget budget = getBudgetByIdOrThrow(budgetId, currentAccount);
        List<Category> categories = budget.getCategories() == null ? Collections.emptyList() : budget.getCategories();
        log.info("Retrieved {} categories for budget ID: {}", categories.size(), budgetId);
        return categories;
    }

    public List<SubCategory> getSubCategoriesForCategoryInBudget(Account currentAccount, String budgetId, String categoryId) {
        log.debug("Fetching subcategories for category ID: {} in budget ID: {}", categoryId, budgetId);
        Budget budget = getBudgetByIdOrThrow(budgetId, currentAccount);
        boolean isCategoryInBudget = budget.getCategories().stream().anyMatch(category -> Objects.equals(category.getId(), categoryId));
        if (!isCategoryInBudget) {
            log.warn("Attempted to fetch subcategories for a non-existing category ID: {} in budget ID: {}", categoryId, budgetId);
            throw new IllegalArgumentException("Budget does not contains this category");
        }
        Category category = categoryService.getCategory(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with ID: {}", categoryId);
                    return new ItemNotFound("Category was not found or you do not have enough permissions");
                });
        List<SubCategory> subCategories = category.getSubCategories() == null ? Collections.emptyList() : category.getSubCategories();
        log.info("Retrieved {} subcategories for category ID: {} in budget ID: {}", subCategories.size(), categoryId, budgetId);

        return subCategories;
    }

    public Category patchCategory(Account currentAccount, Category patchedCategory) {
        log.debug("Patching category with id={} by accountId={}", patchedCategory.getId(), currentAccount.getId());
        Category oryginalCategory = categoryService.getCategory(patchedCategory.getId())
                .orElseThrow(() -> {
                    log.warn("Category not found with id={}", patchedCategory.getId());
                    return new ItemNotFound("Category was not found or you do not have enough permissions");
                });
        oryginalCategory.setName(patchedCategory.getName());
        oryginalCategory.setAllocatedAmount(patchedCategory.getAllocatedAmount());
        if (patchedCategory.getType() != null) {
            oryginalCategory.setType(patchedCategory.getType());
        }
        Category savedCategory = categoryService.saveCategory(oryginalCategory);
        log.info("Category with id={} has been updated by accountId={}", savedCategory.getId(), currentAccount.getId());
        return savedCategory;
    }

    public void deleteCategoryReferenceFromAllBudgets(Account currentAccount, String categoryId) {
        budgetRepository.removeCategoryFromBudget(categoryId);
        log.info("Category id={} was removed from all Budget references in property '{}' by accountId={}", categoryId, Budget.FIELD_NAME_CATEGORIES, currentAccount.getId());
    }
}
