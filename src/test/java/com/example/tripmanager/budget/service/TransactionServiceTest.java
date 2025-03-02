package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.TransactionMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.Transaction;
import com.example.tripmanager.budget.model.TransactionBudgetSummary;
import com.example.tripmanager.budget.model.TransactionCreateForm;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.repository.TransactionRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testCreateTransaction_Success_NoCategory() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");

        Budget budget = new Budget();
        budget.setCategories(Collections.emptyList());
        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        Transaction transaction = new Transaction();
        try (var mapperMock = mockStatic(TransactionMapper.class)) {
            mapperMock.when(() -> TransactionMapper.transactionFromCreateForm(form))
                    .thenReturn(transaction);
            when(transactionRepository.save(transaction)).thenReturn(transaction);

            Transaction result = transactionService.createTransaction(form, account);
            assertEquals(transaction, result);
            mapperMock.verify(() -> TransactionMapper.transactionFromCreateForm(form), times(1));
            verify(transactionRepository, times(1)).save(transaction);
        }
    }

    @Test
    void testCreateTransaction_Success_WithCategory() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");
        form.setCategoryId("cat1");

        Category category = new Category();
        category.setId("cat1");
        Budget budget = new Budget();
        budget.setCategories(List.of(category));
        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        Transaction transaction = new Transaction();
        try (var mapperMock = mockStatic(TransactionMapper.class)) {
            mapperMock.when(() -> TransactionMapper.transactionFromCreateForm(form))
                    .thenReturn(transaction);
            when(transactionRepository.save(transaction)).thenReturn(transaction);

            Transaction result = transactionService.createTransaction(form, account);
            assertEquals(transaction, result);
            mapperMock.verify(() -> TransactionMapper.transactionFromCreateForm(form), times(1));
            verify(transactionRepository, times(1)).save(transaction);
        }
    }

    @Test
    void testCreateTransaction_Success_WithSubCategory() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");

        String categoryId = new ObjectId().toHexString();
        String subCategoryId = new ObjectId().toHexString();

        form.setCategoryId(categoryId);
        form.setSubCategoryId(subCategoryId);

        SubCategory subCategory = new SubCategory();
        subCategory.setId(subCategoryId);

        Category category = new Category();
        category.setId(categoryId);
        category.setSubCategories(List.of(subCategory));

        Budget budget = new Budget();
        budget.setCategories(List.of(category));

        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        Transaction transaction = new Transaction();
        try (var mapperMock = mockStatic(TransactionMapper.class)) {
            mapperMock.when(() -> TransactionMapper.transactionFromCreateForm(form))
                    .thenReturn(transaction);
            when(transactionRepository.save(transaction)).thenReturn(transaction);

            Transaction result = transactionService.createTransaction(form, account);

            assertEquals(transaction, result);
            mapperMock.verify(() -> TransactionMapper.transactionFromCreateForm(form), times(1));
            verify(transactionRepository, times(1)).save(transaction);
        }
    }

    @Test
    void testCreateTransaction_Fail_InvalidBudget() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("invalidBudget");

        when(budgetService.getBudgetById("invalidBudget", account)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(form, account));
        assertEquals("Wrong BudgetId", exception.getMessage());
        verify(budgetService, times(1)).getBudgetById("invalidBudget", account);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_Fail_SubCategoryWithoutCategory() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");
        form.setSubCategoryId("sub1");
        form.setCategoryId("");

        Budget budget = new Budget();
        budget.setCategories(Collections.emptyList());
        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(form, account));
        assertEquals("SubCategoryId cannot be selected because CategoryId is blank", exception.getMessage());
        verify(budgetService, times(1)).getBudgetById("budget1", account);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_Fail_WrongCategoryId() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");
        form.setCategoryId("nonexistentCat");

        Category category = new Category();
        category.setId("cat1");
        Budget budget = new Budget();
        budget.setCategories(List.of(category));
        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(form, account));
        assertEquals("Wrong CategoryId - Budget does not contains this category", exception.getMessage());
        verify(budgetService, times(1)).getBudgetById("budget1", account);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_Fail_WrongSubCategoryId() {
        Account account = new Account();
        TransactionCreateForm form = new TransactionCreateForm();
        form.setBudgetId("budget1");

        String categoryId = new ObjectId().toHexString();
        String validSubCategoryId = new ObjectId().toHexString();
        String nonexistentSubCategoryId = new ObjectId().toHexString();

        form.setCategoryId(categoryId);
        form.setSubCategoryId(nonexistentSubCategoryId);

        SubCategory subCategory = new SubCategory();
        subCategory.setId(validSubCategoryId);

        Category category = new Category();
        category.setId(categoryId);
        category.setSubCategories(List.of(subCategory));

        Budget budget = new Budget();
        budget.setCategories(List.of(category));

        when(budgetService.getBudgetById("budget1", account)).thenReturn(budget);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(form, account));

        assertEquals("Wrong SubCategoryId - Budget does not contains this subCategory", exception.getMessage());

        verify(budgetService, times(1)).getBudgetById("budget1", account);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testGetTransactionsForBudget() {
        Account account = new Account();
        String budgetId = "budget1";
        String categoryId = "cat1";
        String subCategoryId = "sub1";
        Pageable pageable = mock(Pageable.class);

        Budget budget = new Budget();
        when(budgetService.getBudgetById(budgetId, account)).thenReturn(budget);

        Page<Transaction> page = new PageImpl<>(List.of(new Transaction(), new Transaction()));
        when(transactionRepository.getTransactionByBudgetIdAndCategoryId(
                pageable, budgetId, categoryId, subCategoryId, true, false))
                .thenReturn(page);

        Page<Transaction> result = transactionService.getTransactionsForBudget(pageable, account, budgetId, categoryId, subCategoryId, true, false);
        assertEquals(2, result.getContent().size());
        verify(budgetService, times(1)).getBudgetById(budgetId, account);
        verify(transactionRepository, times(1))
                .getTransactionByBudgetIdAndCategoryId(pageable, budgetId, categoryId, subCategoryId, true, false);
    }

    @Test
    void testGetTransactionsStatsForBudget_Found() {
        Account account = new Account();
        String budgetId = "budget1";
        Budget budget = new Budget();
        when(budgetService.getBudgetById(budgetId, account)).thenReturn(budget);

        TransactionBudgetSummary summary = new TransactionBudgetSummary(budgetId, 100, 50);
        when(transactionRepository.getTransactionBudgetSummaryByBudgetId(budgetId))
                .thenReturn(Optional.of(summary));

        TransactionBudgetSummary result = transactionService.getTransactionsStatsForBudget(budgetId, account);
        assertEquals(summary, result);
        verify(budgetService, times(1)).getBudgetById(budgetId, account);
        verify(transactionRepository, times(1)).getTransactionBudgetSummaryByBudgetId(budgetId);
    }

    @Test
    void testGetTransactionsStatsForBudget_NotFound() {
        Account account = new Account();
        String budgetId = "budget1";
        Budget budget = new Budget();
        when(budgetService.getBudgetById(budgetId, account)).thenReturn(budget);

        when(transactionRepository.getTransactionBudgetSummaryByBudgetId(budgetId))
                .thenReturn(Optional.empty());

        TransactionBudgetSummary result = transactionService.getTransactionsStatsForBudget(budgetId, account);
        assertNotNull(result);
        assertEquals(budgetId, result.getBudgetId());
        verify(budgetService, times(1)).getBudgetById(budgetId, account);
        verify(transactionRepository, times(1)).getTransactionBudgetSummaryByBudgetId(budgetId);
    }
}
