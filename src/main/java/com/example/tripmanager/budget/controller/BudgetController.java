package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.model.InviteMembersRequest;
import com.example.tripmanager.budget.model.category.*;
import com.example.tripmanager.budget.service.BudgetService;
import com.example.tripmanager.budget.service.CategoryService;
import com.example.tripmanager.budget.service.TransactionService;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import com.example.tripmanager.shared.model.messageResponse.MessageResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(BudgetController.CONTROLLER_URL)
public class BudgetController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/budgets";
    private static final Logger log = LoggerFactory.getLogger(BudgetController.class);

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;

    protected BudgetDto toDto(Budget budget) {
        return BudgetMapper.toDto(budget, accountService);
    }

    protected Page<BudgetDto> toDto(Page<Budget> budgets) {
        return BudgetMapper.toDto(budgets, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto createBudget(
            Principal principal,
            @Valid @RequestBody BudgetCreateForm budgetCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to create Budget by accountId={}", currentAccount.getId());
        Budget createdBudget = budgetService.createBudget(budgetCreateForm, currentAccount);
        return toDto(createdBudget);
    }

    @GetMapping
    public Page<BudgetDto> getBudgets(
            Principal principal,
            @ParameterObject PageParams pageParams,
            @RequestParam(required = false, defaultValue = "false") boolean includeArchived
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<Budget> budgets = budgetService.getBudgetsRelatedToAccount(pageParams.asPageable(), currentAccount, includeArchived);
        return toDto(budgets);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @GetMapping("/{budgetId}")
    public BudgetDto getBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget budget = budgetService.getBudgetByIdOrThrow(budgetId, currentAccount);
        return toDto(budget);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PostMapping("/{budgetId}/invite")
    public MessageResponse inviteToBudget(
            Principal principal,
            @PathVariable String budgetId,
            @Valid @RequestBody InviteMembersRequest inviteMembersRequest
    ) {
        Account currentAccount = getCurrentAccount(principal);
        budgetService.inviteMembersToBudget(currentAccount, budgetId, inviteMembersRequest);
        return new MessageResponse("Invitations to budget has been sent");
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PutMapping("/{budgetId}/archive")
    public BudgetDto archiveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to archive Budget {} by accountId={}", budgetId, currentAccount.getId());
        Budget archivedBudget = budgetService.archiveBudget(budgetId, currentAccount);
        return toDto(archivedBudget);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PutMapping("/{budgetId}/unarchive")
    public BudgetDto unArchiveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to unarchive Budget {} by accountId={}", budgetId, currentAccount.getId());
        Budget archivedBudget = budgetService.unArchiveBudget(budgetId, currentAccount);
        return toDto(archivedBudget);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @DeleteMapping("/{budgetId}")
    public void deleteBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to delete Budget {} by accountId={}", budgetId, currentAccount.getId());
        budgetService.deleteBudget(budgetId, currentAccount);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @DeleteMapping("/{budgetId}/leave")
    public void leaveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to leave Budget {} by accountId={}", budgetId, currentAccount.getId());
        budgetService.leaveBudget(budgetId, currentAccount);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PutMapping("/{budgetId}")
    public BudgetDto editBudget(
            Principal principal,
            @PathVariable String budgetId,
            @Valid @RequestBody BudgetCreateForm editedBudget
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to edit Budget {} by accountId={}", budgetId, currentAccount.getId());
        Budget updatedBudget = budgetService.editBudget(budgetId, editedBudget, currentAccount);
        return toDto(updatedBudget);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PostMapping("/{budgetId}/category")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto addCategoryToBudget(
            Principal principal,
            @PathVariable String budgetId,
            @Valid @RequestBody CategoryCreateForm categoryCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to add category to Budget {} by accountId={}", budgetId, currentAccount.getId());
        Budget budget = budgetService.addCategoryToBudget(budgetId, categoryCreateForm, currentAccount);
        return toDto(budget);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PostMapping("/{budgetId}/category/{categoryId}/subcategory")
    @ResponseStatus(HttpStatus.CREATED)
    public SubCategory addSubCategoryToCategoryInBudget(
            Principal principal,
            @PathVariable String budgetId,
            @PathVariable String categoryId,
            @Valid @RequestBody SubCategoryCreateForm subCategoryCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to add subcategory to category {} in Budget {} by accountId={}", categoryId, budgetId, currentAccount.getId());
        return budgetService.addSubCategoryToBudget(budgetId, categoryId, subCategoryCreateForm, currentAccount);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @GetMapping("/{budgetId}/category/with-stats")
    public List<CategoryWithStats> getBudgetCategoriesWithStats(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return budgetService.getBudgetCategoriesWithStats(currentAccount, budgetId);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @GetMapping("/{budgetId}/category")
    public List<Category> getBudgetCategories(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return budgetService.getCategoriesForBudget(currentAccount, budgetId);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @GetMapping("/{budgetId}/category/{categoryId}/subcategory")
    public List<SubCategory> getBudgetCategorySubCategory(
            Principal principal,
            @PathVariable String budgetId,
            @PathVariable String categoryId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return budgetService.getSubCategoriesForCategoryInBudget(currentAccount, budgetId, categoryId);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @PatchMapping("/{budgetId}/category")
    public Category patchBudgetCategory(
            Principal principal,
            @PathVariable String budgetId,
            @RequestBody Category patchedCategory
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return this.budgetService.patchCategory(currentAccount, patchedCategory);
    }

    @PreAuthorize("@budgetSecurity.hasAccessToBudget(#principal, #budgetId)")
    @DeleteMapping("/{budgetId}/category/{categoryId}")
    public MessageResponse deleteBudgetCategory(
            Principal principal,
            @PathVariable String budgetId,
            @PathVariable String categoryId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        this.transactionService.removeCategoryIdFromTransactions(categoryId);
        this.categoryService.deleteCategory(currentAccount, categoryId);
        this.budgetService.deleteCategoryReferenceFromAllBudgets(currentAccount, categoryId);
        return new MessageResponse("Category was successfully deleted");
    }
}
