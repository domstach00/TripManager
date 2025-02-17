package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;
import com.example.tripmanager.budget.service.BudgetService;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(BudgetController.CONTROLLER_URL)
public class BudgetController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/budgets";

    @Autowired
    private BudgetService budgetService;

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

    @GetMapping("/{budgetId}")
    public BudgetDto getBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget budget = budgetService.getBudgetById(budgetId, currentAccount);
        return toDto(budget);
    }

    @PutMapping("/{budgetId}/archive")
    public BudgetDto archiveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget archivedBudget = budgetService.archiveBudget(budgetId, currentAccount);
        return toDto(archivedBudget);
    }

    @PutMapping("/{budgetId}/unarchive")
    public BudgetDto unArchiveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget archivedBudget = budgetService.unArchiveBudget(budgetId, currentAccount);
        return toDto(archivedBudget);
    }

    @DeleteMapping("/{budgetId}")
    public void deleteBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        budgetService.deleteBudget(budgetId, currentAccount);
    }

    @DeleteMapping("/{budgetId}/leave")
    public void leaveBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        budgetService.leaveBudget(budgetId, currentAccount);
    }

    @PutMapping("/{budgetId}")
    public BudgetDto editBudget(
            Principal principal,
            @PathVariable String budgetId,
            @Valid @RequestBody BudgetCreateForm editedBudget
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget updatedBudget = budgetService.editBudget(budgetId, editedBudget, currentAccount);
        return toDto(updatedBudget);
    }

    @PostMapping("/{budgetId}/category")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto addCategoryToBudget(
            Principal principal,
            @PathVariable String budgetId,
            @Valid @RequestBody CategoryCreateForm categoryCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Budget budget = budgetService.addCategoryToBudget(budgetId, categoryCreateForm, currentAccount);
        return toDto(budget);
    }

    @PostMapping("/{budgetId}/category/{categoryId}/subcategory")
    public SubCategory addSubCategoryToCategory(
            Principal principal,
            @PathVariable String budgetId,
            @PathVariable String categoryId,
            @Valid @RequestBody SubCategoryCreateForm subCategoryCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return budgetService.addSubCategoryToBudget(budgetId, categoryId, subCategoryCreateForm, currentAccount);
    }
}
