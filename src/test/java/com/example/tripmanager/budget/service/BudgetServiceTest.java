package com.example.tripmanager.budget.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.budget.mapper.BudgetMapper;
import com.example.tripmanager.budget.mapper.CategoryMapper;
import com.example.tripmanager.budget.model.Budget;
import com.example.tripmanager.budget.model.BudgetCreateForm;
import com.example.tripmanager.budget.model.BudgetTemplate;
import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.budget.model.category.CategoryCreateForm;
import com.example.tripmanager.budget.model.category.SubCategory;
import com.example.tripmanager.budget.model.category.SubCategoryCreateForm;
import com.example.tripmanager.budget.repository.BudgetRepository;
import com.example.tripmanager.shared.exception.ItemNotFound;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void testCreateBudgetFromTemplate() {
        BudgetTemplate template = new BudgetTemplate();
        Budget budgetFromTemplate = new Budget();

        try (var budgetMapperMock = mockStatic(BudgetMapper.class)) {
            budgetMapperMock.when(() -> BudgetMapper.budgetFromTemplate(template))
                    .thenReturn(budgetFromTemplate);
            when(budgetRepository.save(budgetFromTemplate)).thenReturn(budgetFromTemplate);

            Budget result = budgetService.createBudgetFromTemplate(template);

            assertNotNull(result);
            assertEquals(budgetFromTemplate, result);
            budgetMapperMock.verify(() -> BudgetMapper.budgetFromTemplate(template), times(1));
            verify(budgetRepository, times(1)).save(budgetFromTemplate);
        }
    }

    @Test
    void testCreateBudget() {
        BudgetCreateForm createForm = new BudgetCreateForm();
        Account account = new Account();
        Budget budgetToCreate = new Budget();

        try (var budgetMapperMock = mockStatic(BudgetMapper.class)) {
            budgetMapperMock.when(() -> BudgetMapper.budgetFromCreateForm(createForm, account))
                    .thenReturn(budgetToCreate);
            when(budgetRepository.save(budgetToCreate)).thenReturn(budgetToCreate);

            Budget result = budgetService.createBudget(createForm, account);

            assertNotNull(result);
            assertEquals(budgetToCreate, result);
            budgetMapperMock.verify(() -> BudgetMapper.budgetFromCreateForm(createForm, account), times(1));
            verify(budgetRepository, times(1)).save(budgetToCreate);
        }
    }

    @Test
    void testGetBudgetsRelatedToAccount() {
        Account account = new Account();
        Pageable pageable = mock(Pageable.class);
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        Page<Budget> page = new PageImpl<>(budgets);
        when(budgetRepository.getBudgetRelatedWhereGivenAccountIsMember(pageable, account, false)).thenReturn(page);

        Page<Budget> result = budgetService.getBudgetsRelatedToAccount(pageable, account, false);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(budgetRepository, times(1)).getBudgetRelatedWhereGivenAccountIsMember(pageable, account, false);
    }

    @Test
    void testGetBudgetById_Success() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudgetById(budgetId, account);

        assertNotNull(result);
        assertEquals(budget, result);
        verify(budgetRepository, times(1)).getBudgetById(budgetId, account);
    }

    @Test
    void testGetBudgetById_BlankId() {
        Account account = new Account();
        assertThrows(IllegalArgumentException.class, () -> budgetService.getBudgetById("   ", account));
    }

    @Test
    void testGetBudgetById_NotFound() {
        Account account = new Account();
        String budgetId = "1";
        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.empty());

        assertThrows(ItemNotFound.class, () -> budgetService.getBudgetById(budgetId, account));
    }

    @Test
    void testArchiveBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setArchived(false);

        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.archiveBudget(budgetId, account);

        assertTrue(result.isArchived());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testArchiveBudget_AlreadyArchived() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setArchived(true);
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));

        assertThrows(IllegalStateException.class, () -> budgetService.archiveBudget(budgetId, account));
    }

    @Test
    void testArchiveBudget_NotFound() {
        Account account = new Account();
        String budgetId = "1";
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.empty());

        assertThrows(ItemNotFound.class, () -> budgetService.archiveBudget(budgetId, account));
    }

    @Test
    void testUnArchiveBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setArchived(true);

        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.unArchiveBudget(budgetId, account);

        assertFalse(result.isArchived());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testUnArchiveBudget_AlreadyNotArchived() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setArchived(false);
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));

        assertThrows(IllegalStateException.class, () -> budgetService.unArchiveBudget(budgetId, account));
    }

    @Test
    void testUnArchiveBudget_NotFound() {
        Account account = new Account();
        String budgetId = "1";
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.empty());

        assertThrows(ItemNotFound.class, () -> budgetService.unArchiveBudget(budgetId, account));
    }

    @Test
    void testDeleteBudget_NotDeletedYet() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setDeleted(false);
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(budget)).thenReturn(budget);

        budgetService.deleteBudget(budgetId, account);

        assertTrue(budget.isDeleted());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testDeleteBudget_AlreadyDeleted() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setDeleted(true);
        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(budget)).thenReturn(budget);

        budgetService.deleteBudget(budgetId, account);
        verify(budgetRepository, times(1)).save(budget);
    }

    // Test metody leaveBudget
    @Test
    void testLeaveBudget_Success() {
        Account account = new Account();
        account.setId("507f1f77bcf86cd799439011");
        String budgetId = "1";
        Budget budget = new Budget();
        ObjectId accountObjectId = new ObjectId(account.getId());
        List<ObjectId> members = new ArrayList<>();
        members.add(accountObjectId);
        members.add(new ObjectId());
        budget.setMembers(members);

        when(budgetRepository.getBudgetByIdWhereAccountIsMember(budgetId, account))
                .thenReturn(Optional.of(budget));
        when(budgetRepository.save(budget)).thenReturn(budget);

        budgetService.leaveBudget(budgetId, account);

        assertFalse(budget.getMembers().contains(accountObjectId));
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testLeaveBudget_NoChange() {
        Account account = new Account();
        account.setId("507f1f77bcf86cd799439011");
        String budgetId = "1";
        Budget budget = new Budget();
        List<ObjectId> members = new ArrayList<>();
        members.add(new ObjectId());
        budget.setMembers(members);

        when(budgetRepository.getBudgetByIdWhereAccountIsMember(budgetId, account))
                .thenReturn(Optional.of(budget));

        budgetService.leaveBudget(budgetId, account);
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void testLeaveBudget_NullParameters() {
        Account account = new Account();
        assertThrows(IllegalArgumentException.class, () -> budgetService.leaveBudget(null, account));
        assertThrows(IllegalArgumentException.class, () -> budgetService.leaveBudget("1", null));
    }

    @Test
    void testEditBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        BudgetCreateForm form = new BudgetCreateForm();
        form.setName("New Name");
        form.setDescription("New Description");
        form.setAllocatedBudget(BigDecimal.valueOf(1000.0));
        form.setStartDate(LocalDate.of(2025, 1, 1));
        form.setEndDate(LocalDate.of(2025, 12, 31));

        Budget originalBudget = new Budget();
        originalBudget.setName("Old Name");
        originalBudget.setDescription("Old Description");
        originalBudget.setAllocatedBudget(BigDecimal.valueOf(500.0));

        when(budgetRepository.getBudgetByIdWhereAccountIsOwner(budgetId, account))
                .thenReturn(Optional.of(originalBudget));
        when(budgetRepository.save(originalBudget)).thenReturn(originalBudget);

        Budget result = budgetService.editBudget(budgetId, form, account);

        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(1000.0), result.getAllocatedBudget());
        assertNotNull(result.getStartDate());
        assertNotNull(result.getEndDate());
        verify(budgetRepository, times(1)).save(originalBudget);
    }

    @Test
    void testEditBudget_NullParameters() {
        Account account = new Account();
        BudgetCreateForm form = new BudgetCreateForm();

        assertThrows(IllegalArgumentException.class, () -> budgetService.editBudget(null, form, account));
        assertThrows(IllegalArgumentException.class, () -> budgetService.editBudget("1", null, account));
        assertThrows(IllegalArgumentException.class, () -> budgetService.editBudget("1", form, null));
    }

    @Test
    void testAddCategoryToBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setCategories(new ArrayList<>());
        CategoryCreateForm categoryForm = new CategoryCreateForm();
        categoryForm.setName("Category1");

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));

        Category category = new Category();
        try (var categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.categoryFromCreateForm(categoryForm))
                    .thenReturn(category);
            when(categoryService.createCategory(category)).thenReturn(category);
            when(budgetRepository.save(budget)).thenReturn(budget);

            Budget result = budgetService.addCategoryToBudget(budgetId, categoryForm, account);
            assertTrue(result.getCategories().contains(category));
            categoryMapperMock.verify(() -> CategoryMapper.categoryFromCreateForm(categoryForm), times(1));
            verify(categoryService, times(1)).createCategory(category);
            verify(budgetRepository, times(1)).save(budget);
        }
    }

    @Test
    void testAddCategoryToBudget_DuplicateName() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        Category existingCategory = new Category();
        existingCategory.setName("Category1");
        budget.setCategories(new ArrayList<>(Collections.singletonList(existingCategory)));

        CategoryCreateForm categoryForm = new CategoryCreateForm();
        categoryForm.setName("category1");

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));
        assertThrows(IllegalArgumentException.class, () -> budgetService.addCategoryToBudget(budgetId, categoryForm, account));
    }

    @Test
    void testAddSubCategoryToBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        String categoryId = "cat1";
        Budget budget = new Budget();
        Category category = new Category();
        category.setId(categoryId);
        category.setSubCategories(new ArrayList<>());
        budget.setCategories(new ArrayList<>(Collections.singletonList(category)));

        SubCategoryCreateForm subCategoryForm = new SubCategoryCreateForm();

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));
        when(categoryService.getCategory(categoryId)).thenReturn(Optional.of(category));

        SubCategory subCategory = new SubCategory();
        try (var categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.subCategoryFromCreateForm(subCategoryForm))
                    .thenReturn(subCategory);
            when(categoryService.saveCategory(category)).thenReturn(category);

            SubCategory result = budgetService.addSubCategoryToBudget(budgetId, categoryId, subCategoryForm, account);
            assertTrue(category.getSubCategories().contains(subCategory));
            assertEquals(subCategory, result);
            categoryMapperMock.verify(() -> CategoryMapper.subCategoryFromCreateForm(subCategoryForm), times(1));
            verify(categoryService, times(1)).saveCategory(category);
        }
    }

    @Test
    void testAddSubCategoryToBudget_CategoryNotInBudget() {
        Account account = new Account();
        String budgetId = "1";
        String categoryId = "cat1";
        Budget budget = new Budget();
        budget.setCategories(new ArrayList<>());

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));
        SubCategoryCreateForm subCategoryForm = new SubCategoryCreateForm();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> budgetService.addSubCategoryToBudget(budgetId, categoryId, subCategoryForm, account));
        assertEquals("Budget does not contains this category", exception.getMessage());
    }

    @Test
    void testAddSubCategoryToBudget_CategoryNotFound() {
        Account account = new Account();
        String budgetId = "1";
        String categoryId = "cat1";
        Budget budget = new Budget();
        Category category = new Category();
        category.setId(categoryId);
        budget.setCategories(new ArrayList<>(Collections.singletonList(category)));

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));
        when(categoryService.getCategory(categoryId)).thenReturn(Optional.empty());

        SubCategoryCreateForm subCategoryForm = new SubCategoryCreateForm();
        assertThrows(ItemNotFound.class,
                () -> budgetService.addSubCategoryToBudget(budgetId, categoryId, subCategoryForm, account));
    }

    @Test
    void testGetCategoriesForBudget_WithCategories() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        List<Category> categories = Arrays.asList(new Category(), new Category());
        budget.setCategories(categories);

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));

        List<Category> result = budgetService.getCategoriesForBudget(account, budgetId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCategoriesForBudget_NoCategories() {
        Account account = new Account();
        String budgetId = "1";
        Budget budget = new Budget();
        budget.setCategories(null);

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));

        List<Category> result = budgetService.getCategoriesForBudget(account, budgetId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSubCategoriesForCategoryInBudget_Success() {
        Account account = new Account();
        String budgetId = "1";
        String categoryId = "cat1";
        Budget budget = new Budget();
        Category category = new Category();
        category.setId(categoryId);
        List<SubCategory> subCategories = Arrays.asList(new SubCategory(), new SubCategory());
        category.setSubCategories(subCategories);
        budget.setCategories(new ArrayList<>(Collections.singletonList(category)));

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));
        when(categoryService.getCategory(categoryId)).thenReturn(Optional.of(category));

        List<SubCategory> result = budgetService.getSubCategoriesForCategoryInBudget(account, budgetId, categoryId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetSubCategoriesForCategoryInBudget_CategoryNotInBudget() {
        Account account = new Account();
        String budgetId = "1";
        String categoryId = "cat1";
        Budget budget = new Budget();
        budget.setCategories(new ArrayList<>());

        when(budgetRepository.getBudgetById(budgetId, account)).thenReturn(Optional.of(budget));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> budgetService.getSubCategoriesForCategoryInBudget(account, budgetId, categoryId));
        assertEquals("Budget does not contains this category", exception.getMessage());
    }
}