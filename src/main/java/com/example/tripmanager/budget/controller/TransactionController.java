package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.TransactionMapper;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.TransactionDto;
import com.example.tripmanager.budget.service.TransactionService;
import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(TransactionController.CONTROLLER_URL)
public class TransactionController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/transactions";
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    protected TransactionDto toDto(Transaction transaction) {
        return TransactionMapper.toDto(transaction, accountService);
    }

    protected Page<TransactionDto> toDto(Page<Transaction> transactions) {
        return TransactionMapper.toDto(transactions, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto createTransaction(
            Principal principal,
            @Valid @RequestBody TransactionCreateForm transactionCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to create Transaction by accountId={}", currentAccount.getId());
        Transaction createdTransaction = transactionService.createTransaction(transactionCreateForm, currentAccount);
        return toDto(createdTransaction);
    }

    @GetMapping("/budgets/{budgetId}")
    public Page<TransactionDto> getTransactionsForBudget(
            Principal principal,
            @PathVariable String budgetId,
            @ParameterObject PageParams pageParams,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String subCategoryId,
            @RequestParam(required = false, defaultValue = "false") boolean excludeCategorized,
            @RequestParam(required = false, defaultValue = "false") boolean excludeSubCategorized
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<Transaction> transactions = transactionService.getTransactionsForBudget(pageParams.asPageable(), currentAccount, budgetId, categoryId, subCategoryId, excludeCategorized, excludeSubCategorized);
        return toDto(transactions);
    }

    @GetMapping("/budgets/{budgetId}/summary")
    public TransactionBudgetSummary getTransactionSummaryForGivenBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return transactionService.getTransactionsStatsForBudget(budgetId, currentAccount);
    }
}
