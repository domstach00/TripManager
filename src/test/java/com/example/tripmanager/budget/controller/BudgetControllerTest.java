package com.example.tripmanager.budget.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetDto;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;
import com.example.tripmanager.budget.service.BudgetService;
import com.example.tripmanager.shared.controller.support.PageParams;
import org.bson.types.ObjectId;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {
    @Mock
    private BudgetService budgetService;
    @Mock
    private com.example.tripmanager.account.service.AccountService accountService;
    @InjectMocks
    @Spy
    private BudgetController budgetController;
    private Account dummyAccount;
    private Principal dummyPrincipal;
    @BeforeEach
    void setUp() {
        dummyAccount = new Account();
        dummyAccount.setId("acc1");
        when(accountService.getCurrentAccount(any(Principal.class))).thenReturn(dummyAccount);
        dummyPrincipal = () -> "user@example.com";
    }
    @Test
    void testCreateBudget() {
        BudgetCreateForm form = new BudgetCreateForm();
        form.setName("Test Budget");
        Budget budget = new Budget();
        budget.setId("b1");
        budget.setName("Test Budget");
        when(budgetService.createBudget(eq(form), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId("b1");
        dto.setName("Test Budget");
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.createBudget(dummyPrincipal, form);
        assertNotNull(result);
        assertEquals("b1", result.getId());
        assertEquals("Test Budget", result.getName());
        verify(budgetService, times(1)).createBudget(eq(form), eq(dummyAccount));
    }
    @Test
    void testGetBudgets() {
        PageParams params = new PageParams();
        Pageable pageable = params.asPageable();
        Budget budget1 = new Budget();
        budget1.setId("b1");
        Budget budget2 = new Budget();
        budget2.setId("b2");
        Page<Budget> page = new PageImpl<>(Arrays.asList(budget1, budget2));
        when(budgetService.getBudgetsRelatedToAccount(eq(pageable), eq(dummyAccount), eq(false))).thenReturn(page);
        BudgetDto dto1 = new BudgetDto();
        dto1.setId("b1");
        BudgetDto dto2 = new BudgetDto();
        dto2.setId("b2");
        Page<BudgetDto> dtoPage = new PageImpl<>(Arrays.asList(dto1, dto2));
        doReturn(dtoPage).when(budgetController).toDto(page);
        Page<BudgetDto> result = budgetController.getBudgets(dummyPrincipal, params, false);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(budgetService, times(1)).getBudgetsRelatedToAccount(eq(pageable), eq(dummyAccount), eq(false));
    }
    @Test
    void testGetBudget() {
        String budgetId = "b1";
        Budget budget = new Budget();
        budget.setId(budgetId);
        when(budgetService.getBudgetByIdOrThrow(eq(budgetId), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId(budgetId);
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.getBudget(dummyPrincipal, budgetId);
        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetService, times(1)).getBudgetByIdOrThrow(eq(budgetId), eq(dummyAccount));
    }
    @Test
    void testArchiveBudget() {
        String budgetId = "b1";
        Budget budget = new Budget();
        budget.setId(budgetId);
        when(budgetService.archiveBudget(eq(budgetId), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId(budgetId);
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.archiveBudget(dummyPrincipal, budgetId);
        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetService, times(1)).archiveBudget(eq(budgetId), eq(dummyAccount));
    }
    @Test
    void testUnArchiveBudget() {
        String budgetId = "b1";
        Budget budget = new Budget();
        budget.setId(budgetId);
        when(budgetService.unArchiveBudget(eq(budgetId), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId(budgetId);
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.unArchiveBudget(dummyPrincipal, budgetId);
        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetService, times(1)).unArchiveBudget(eq(budgetId), eq(dummyAccount));
    }
    @Test
    void testDeleteBudget() {
        String budgetId = "b1";
        budgetController.deleteBudget(dummyPrincipal, budgetId);
        verify(budgetService, times(1)).deleteBudget(eq(budgetId), eq(dummyAccount));
    }
    @Test
    void testLeaveBudget() {
        String budgetId = "b1";
        budgetController.leaveBudget(dummyPrincipal, budgetId);
        verify(budgetService, times(1)).leaveBudget(eq(budgetId), eq(dummyAccount));
    }
    @Test
    void testEditBudget() {
        String budgetId = "b1";
        BudgetCreateForm form = new BudgetCreateForm();
        form.setName("Edited Budget");
        Budget budget = new Budget();
        budget.setId(budgetId);
        budget.setName("Edited Budget");
        when(budgetService.editBudget(eq(budgetId), eq(form), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId(budgetId);
        dto.setName("Edited Budget");
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.editBudget(dummyPrincipal, budgetId, form);
        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        assertEquals("Edited Budget", result.getName());
        verify(budgetService, times(1)).editBudget(eq(budgetId), eq(form), eq(dummyAccount));
    }
    @Test
    void testAddCategoryToBudget() {
        String budgetId = "b1";
        CategoryCreateForm form = new CategoryCreateForm();
        form.setName("Category1");
        Budget budget = new Budget();
        budget.setId(budgetId);
        when(budgetService.addCategoryToBudget(eq(budgetId), eq(form), eq(dummyAccount))).thenReturn(budget);
        BudgetDto dto = new BudgetDto();
        dto.setId(budgetId);
        doReturn(dto).when(budgetController).toDto(budget);
        BudgetDto result = budgetController.addCategoryToBudget(dummyPrincipal, budgetId, form);
        assertNotNull(result);
        assertEquals(budgetId, result.getId());
        verify(budgetService, times(1)).addCategoryToBudget(eq(budgetId), eq(form), eq(dummyAccount));
    }
    @Test
    void testAddSubCategoryToCategoryInBudget() {
        String budgetId = "b1";
        String categoryId = "c1";
        SubCategoryCreateForm form = new SubCategoryCreateForm();
        form.setName("SubCategory1");
        SubCategory subCategory = new SubCategory();
        String subCategoryId = new ObjectId().toHexString();
        subCategory.setId(subCategoryId);
        when(budgetService.addSubCategoryToBudget(eq(budgetId), eq(categoryId), eq(form), eq(dummyAccount)))
                .thenReturn(subCategory);
        SubCategory result = budgetController.addSubCategoryToCategoryInBudget(dummyPrincipal, budgetId, categoryId, form);
        assertNotNull(result);
        assertEquals(subCategoryId, result.getId());
        verify(budgetService, times(1)).addSubCategoryToBudget(eq(budgetId), eq(categoryId), eq(form), eq(dummyAccount));
    }
    @Test
    void testGetBudgetCategories() {
        String budgetId = "b1";
        Category cat1 = new Category();
        cat1.setId("c1");
        List<Category> list = Collections.singletonList(cat1);
        when(budgetService.getCategoriesForBudget(eq(dummyAccount), eq(budgetId))).thenReturn(list);
        List<Category> result = budgetController.getBudgetCategories(dummyPrincipal, budgetId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("c1", result.get(0).getId());
        verify(budgetService, times(1)).getCategoriesForBudget(eq(dummyAccount), eq(budgetId));
    }
    @Test
    void testGetBudgetCategorySubCategory() {
        String budgetId = "b1";
        String categoryId = "c1";
        SubCategory sub1 = new SubCategory();
        String sub1Id = new ObjectId().toHexString();
        sub1.setId(sub1Id);
        List<SubCategory> list = Collections.singletonList(sub1);
        when(budgetService.getSubCategoriesForCategoryInBudget(eq(dummyAccount), eq(budgetId), eq(categoryId)))
                .thenReturn(list);
        List<SubCategory> result = budgetController.getBudgetCategorySubCategory(dummyPrincipal, budgetId, categoryId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sub1Id, result.get(0).getId());
        verify(budgetService, times(1)).getSubCategoriesForCategoryInBudget(eq(dummyAccount), eq(budgetId), eq(categoryId));
    }
}
