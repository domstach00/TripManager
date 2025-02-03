package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.repository.BudgetRepository;
import com.example.tripmanager.shared.exception.ItemNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

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
}
