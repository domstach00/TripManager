package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.TransactionMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.repository.TransactionRepository;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BudgetService budgetService;

    @Transactional
    public Transaction createTransaction(TransactionCreateForm transactionCreateForm, Account currentAccount) {
        log.info("Creating a new transaction for budget {} by {}", transactionCreateForm.getBudgetId(), currentAccount.getEmail());
        validateTransactionCreateForm(transactionCreateForm, currentAccount);
        Transaction transactionToCreate = TransactionMapper.transactionFromCreateForm(transactionCreateForm);
        Transaction createdTransaction = transactionRepository.save(transactionToCreate);
        log.info("Transaction successfully created with ID: {}", transactionToCreate.getId());
        return createdTransaction;
    }

    public Page<Transaction> getTransactionsForBudget(Pageable pageable, Account currentAccount, String budgetId, @Nullable String categoryId, @Nullable String subCategoryId, boolean excludeCategorized, boolean excludeSubCategorized) {
        log.debug("Fetching transactions for budgetId: {}, categoryId: {}, subCategoryId: {}", budgetId, categoryId, subCategoryId);
        budgetService.getBudgetByIdOrThrow(budgetId, currentAccount);
        Page<Transaction> transactions = transactionRepository.getTransactionByBudgetIdAndCategoryId(pageable, budgetId, categoryId, subCategoryId, excludeCategorized, excludeSubCategorized);
        log.info("Retrieved {} transactions for budgetId: {}", transactions.getTotalElements(), budgetId);
        return transactions;
    }

    public TransactionBudgetSummary getTransactionsStatsForBudget(String budgetId, Account currentAccount) {
        log.debug("Fetching transaction statistics for budgetId: {}", budgetId);
        budgetService.getBudgetByIdOrThrow(budgetId, currentAccount); // to check if account has access to budget
        Optional<TransactionBudgetSummary> budgetSummaryOpt = transactionRepository.getTransactionBudgetSummaryByBudgetId(budgetId);
        TransactionBudgetSummary summary = budgetSummaryOpt.orElseGet(() -> new TransactionBudgetSummary(budgetId, 0, 0));
        log.info("Transaction statistics retrieved for budgetId: {} - Total Amount: {}, Total Transactions: {}", budgetId, summary.getTransactionCount(), summary.getTransactionCount());
        return summary;
    }

    private void validateTransactionCreateForm(TransactionCreateForm createForm, Account currentAccount) {
        log.debug("Validating transaction create form for account: {}", currentAccount.getEmail());
        Budget refferedBudget = budgetService.getBudgetByIdOrThrow(createForm.getBudgetId(), currentAccount);
        if (refferedBudget == null) {
            log.warn("Invalid transaction: BudgetId {} does not exist or user has no access", createForm.getBudgetId());
            throw new IllegalArgumentException("Wrong BudgetId");
        }
        // When SubCategory is selected (and Category)
        if (!StringUtils.isBlank(createForm.getSubCategoryId())) {
            if (StringUtils.isBlank(createForm.getCategoryId())) {
                log.warn("Invalid transaction: SubCategoryId {} provided but CategoryId is missing", createForm.getSubCategoryId());
                throw new IllegalArgumentException("SubCategoryId cannot be selected because CategoryId is blank");
            }
            Category selectedCategory = refferedBudget.getCategories().stream()
                    .filter(category -> Objects.equals(category.getId(), createForm.getCategoryId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("Invalid transaction: CategoryId {} does not exist in budget {}", createForm.getCategoryId(), createForm.getBudgetId());
                        return new IllegalArgumentException("Wrong CategoryId - Budget does not contains this category");
                    });

            List<String> subCategoryIdList = selectedCategory.getSubCategories().stream()
                    .map(SubCategory::getId)
                    .toList();
            if (subCategoryIdList.isEmpty() || !subCategoryIdList.contains(createForm.getSubCategoryId())) {
                log.warn("Invalid transaction: SubCategoryId {} does not exist in CategoryId {}", createForm.getSubCategoryId(), createForm.getCategoryId());
                throw new IllegalArgumentException("Wrong SubCategoryId - Budget does not contains this subCategory");
            }
        }
        // When only Category is selected
        else if (!StringUtils.isBlank(createForm.getCategoryId())) {
            if (refferedBudget.getCategories() == null || refferedBudget.getCategories().isEmpty()) {
                log.warn("Invalid transaction: CategoryId {} does not exist in budget {}", createForm.getCategoryId(), createForm.getBudgetId());
                throw new IllegalArgumentException("Wrong CategoryId");
            }
            List<String> categoryIdList = refferedBudget.getCategories().stream()
                    .map(AbstractEntity::getId)
                    .toList();
            if (categoryIdList.isEmpty() || !categoryIdList.contains(createForm.getCategoryId())) {
                log.warn("Invalid transaction: CategoryId {} does not exist in budget {}", createForm.getCategoryId(), createForm.getBudgetId());
                throw new IllegalArgumentException("Wrong CategoryId - Budget does not contains this category");
            }
        }
        log.info("Transaction create form validation successful for budgetId: {}", createForm.getBudgetId());
    }

    public void removeCategoryIdFromTransactions(String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            log.warn("Attempt to remove categories from transaction but categoryId={} is blank", categoryId);
            throw new IllegalArgumentException("CategoryId or BudgetId is blank");
        }
        log.debug("Removing category id={} from transactions", categoryId);
        UpdateResult updateResult = transactionRepository.deleteCategoryFromTransactionsInBudget(categoryId);
        log.info("Removed categoryId={} from transactions, number of modified transactions={}", categoryId, updateResult.getModifiedCount());
    }

    public BigDecimal getTotalAmountForCategory(String budgetId, String categoryId) {
        return transactionRepository.getTotalAmountForCategory(budgetId, categoryId);
    }
}
