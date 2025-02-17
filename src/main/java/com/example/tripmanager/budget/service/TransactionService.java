package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.TransactionMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.repository.TransactionRepository;
import com.example.tripmanager.shared.model.AbstractEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BudgetService budgetService;

    @Transactional
    public Transaction createTransaction(TransactionCreateForm transactionCreateForm, Account currentAccount) {
        validateTransactionCreateForm(transactionCreateForm, currentAccount);
        Transaction transactionToCreate = TransactionMapper.transactionFromCreateForm(transactionCreateForm);
        return transactionRepository.save(transactionToCreate);
    }

    public Page<Transaction> getTransactionsForBudget(Pageable pageable, Account currentAccount, String budgetId, @Nullable String categoryId) {
        budgetService.getBudgetById(budgetId, currentAccount);
        return transactionRepository.getTransactionByBudgetIdAndCategoryId(pageable, budgetId, categoryId);
    }

    public TransactionBudgetSummary getTransactionsStatsForBudget(String budgetId, Account currentAccount) {
        budgetService.getBudgetById(budgetId, currentAccount); // to check if account has access to budget
        Optional<TransactionBudgetSummary> budgetSummaryOpt = transactionRepository.getTransactionBudgetSummaryByBudgetId(budgetId);
        return budgetSummaryOpt.orElseGet(() -> new TransactionBudgetSummary(budgetId, 0, 0));
    }

    private void validateTransactionCreateForm(TransactionCreateForm createForm, Account currentAccount) {
        Budget refferedBudget = budgetService.getBudgetById(createForm.getBudgetId(), currentAccount);
        if (refferedBudget == null) {
            throw new IllegalArgumentException("Wrong BudgetId");
        }
        // When SubCategory is selected (and Category)
        if (!StringUtils.isBlank(createForm.getSubCategoryId())) {
            if (StringUtils.isBlank(createForm.getCategoryId())) {
                throw new IllegalArgumentException("SubCategoryId cannot be selected because CategoryId is blank");
            }
            Category selectedCategory = refferedBudget.getCategories().stream()
                    .filter(category -> Objects.equals(category.getId(), createForm.getCategoryId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Wrong CategoryId - Budget does not contains this category"));

            List<String> subCategoryIdList = selectedCategory.getSubCategories().stream()
                    .map(AbstractEntity::getId)
                    .toList();
            if (subCategoryIdList.isEmpty() || !subCategoryIdList.contains(createForm.getSubCategoryId())) {
                throw new IllegalArgumentException("Wrong SubCategoryId - Budget does not contains this subCategory");
            }
        }
        // When only Category is selected
        else if (!StringUtils.isBlank(createForm.getCategoryId())) {
            if (refferedBudget.getCategories() == null || refferedBudget.getCategories().isEmpty()) {
                throw new IllegalArgumentException("Wrong CategoryId");
            }
            List<String> categoryIdList = refferedBudget.getCategories().stream()
                    .map(AbstractEntity::getId)
                    .toList();
            if (categoryIdList.isEmpty() || !categoryIdList.contains(createForm.getCategoryId())) {
                throw new IllegalArgumentException("Wrong CategoryId - Budget does not contains this category");
            }
        }
    }

}
