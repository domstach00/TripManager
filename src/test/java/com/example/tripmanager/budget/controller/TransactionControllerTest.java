package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.TransactionDto;
import com.example.tripmanager.budget.service.TransactionService;
import com.example.tripmanager.shared.controller.support.PageParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private com.example.tripmanager.account.service.AccountService accountService;
    @InjectMocks
    @Spy
    private TransactionController transactionController;
    private Account dummyAccount;
    private Principal dummyPrincipal;

    @BeforeEach
    void setUp() {
        dummyAccount = new Account();
        dummyAccount.setId("acc1");

        when(accountService.getCurrentAccount(any(Principal.class)))
                .thenReturn(dummyAccount);

        dummyPrincipal = () -> "user@example.com";
    }

    @Test
    void testCreateBudget() {
        TransactionCreateForm createForm = new TransactionCreateForm();
        createForm.setBudgetId("bud1");

        Transaction createdTransaction = new Transaction();
        createdTransaction.setId("t1");

        when(transactionService.createTransaction(eq(createForm), eq(dummyAccount)))
                .thenReturn(createdTransaction);

        TransactionDto expectedDto = new TransactionDto();
        expectedDto.setId("t1");

        doReturn(expectedDto)
                .when(transactionController).toDto(createdTransaction);

        TransactionDto result = transactionController.createBudget(dummyPrincipal, createForm);

        assertNotNull(result);
        assertEquals("t1", result.getId());

        verify(transactionService, times(1))
                .createTransaction(eq(createForm), eq(dummyAccount));
    }

    @Test
    void testGetTransactionsForBudget() {
        String budgetId = "bud1";

        PageParams pageParams = new PageParams();
        Pageable pageable = pageParams.asPageable();

        Transaction txn1 = new Transaction();
        txn1.setId("t1");

        Transaction txn2 = new Transaction();
        txn2.setId("t2");

        List<Transaction> txnList = Arrays.asList(txn1, txn2);
        Page<Transaction> transactionPage = new PageImpl<>(txnList);

        when(transactionService.getTransactionsForBudget(
                eq(pageable),
                eq(dummyAccount),
                eq(budgetId),
                anyString(),
                anyString(),
                anyBoolean(),
                anyBoolean()))
                .thenReturn(transactionPage);

        TransactionDto dto1 = new TransactionDto();
        dto1.setId("t1");

        TransactionDto dto2 = new TransactionDto();
        dto2.setId("t2");

        Page<TransactionDto> expectedPage = new PageImpl<>(Arrays.asList(dto1, dto2));

        doReturn(expectedPage)
                .when(transactionController).toDto(transactionPage);

        Page<TransactionDto> result = transactionController.getTransactionsForBudget(
                dummyPrincipal,
                budgetId,
                pageParams,
                "cat1",
                "sub1",
                false,
                false);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("t1", result.getContent().get(0).getId());
        assertEquals("t2", result.getContent().get(1).getId());

        verify(transactionService, times(1))
                .getTransactionsForBudget(eq(pageable), eq(dummyAccount), eq(budgetId),
                        eq("cat1"), eq("sub1"), eq(false), eq(false));
    }

    @Test
    void testGetTransactionSummaryForGivenBudget() {
        String budgetId = "bud1";

        TransactionBudgetSummary summary = new TransactionBudgetSummary(budgetId, 100, 50);

        when(transactionService.getTransactionsStatsForBudget(eq(budgetId), eq(dummyAccount)))
                .thenReturn(summary);

        TransactionBudgetSummary result = transactionController.getTransactionSummaryForGivenBudget(dummyPrincipal, budgetId);

        assertNotNull(result);
        assertEquals(budgetId, result.getBudgetId());
        assertEquals(100, result.getTransactionCount());
        assertEquals(50, result.getTotalTransactionsValue());

        verify(transactionService, times(1))
                .getTransactionsStatsForBudget(eq(budgetId), eq(dummyAccount));
    }

    @Test
    void testCreateBudget_ServiceThrowsException() {
        TransactionCreateForm createForm = new TransactionCreateForm();
        createForm.setBudgetId("bud1");

        when(transactionService.createTransaction(eq(createForm), eq(dummyAccount)))
                .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () ->
                transactionController.createBudget(dummyPrincipal, createForm));
    }

    @Test
    void testGetTransactionsForBudget_EmptyPage() {
        String budgetId = "bud1";

        PageParams pageParams = new PageParams();
        Pageable pageable = pageParams.asPageable();

        Page<Transaction> emptyPage = new PageImpl<>(Collections.emptyList());

        when(transactionService.getTransactionsForBudget(
                eq(pageable),
                eq(dummyAccount),
                eq(budgetId),
                anyString(),
                anyString(),
                anyBoolean(),
                anyBoolean()))
                .thenReturn(emptyPage);

        Page<TransactionDto> emptyDtoPage = new PageImpl<>(Collections.emptyList());

        doReturn(emptyDtoPage)
                .when(transactionController).toDto(emptyPage);

        Page<TransactionDto> result = transactionController.getTransactionsForBudget(
                dummyPrincipal,
                budgetId,
                pageParams,
                "cat1",
                "sub1",
                false,
                false);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void testGetTransactionSummaryForGivenBudget_ServiceThrowsException() {
        String budgetId = "bud1";

        when(transactionService.getTransactionsStatsForBudget(eq(budgetId), eq(dummyAccount)))
                .thenThrow(new RuntimeException("Summary error"));

        assertThrows(RuntimeException.class, () ->
                transactionController.getTransactionSummaryForGivenBudget(dummyPrincipal, budgetId));
    }
}
