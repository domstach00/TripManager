package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.TransactionMapper;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.TransactionDto;
import com.example.tripmanager.budget.service.TransactionService;
import com.example.tripmanager.shared.controller.AbstractController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(TransactionController.CONTROLLER_URL)
public class TransactionController extends AbstractController {
    public static final String CONTROLLER_URL = "/api/transactions";

    @Autowired
    private TransactionService transactionService;

    protected TransactionDto toDto(Transaction transaction) {
        return TransactionMapper.toDto(transaction, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto createBudget(
            Principal principal,
            @Valid @RequestBody TransactionCreateForm transactionCreateForm
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Transaction createdTransaction = transactionService.createTransaction(transactionCreateForm, currentAccount);
        return toDto(createdTransaction);
    }

    @GetMapping("/budgets/{budgetId}")
    @GetMapping("/budgets/{budgetId}/summary")
    public TransactionBudgetSummary getTransactionSummaryForGivenBudget(
            Principal principal,
            @PathVariable String budgetId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        return transactionService.getTransactionsStatsForBudget(budgetId, currentAccount);
    }
}
