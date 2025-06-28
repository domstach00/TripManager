import { Injectable } from "@angular/core";
import { ApiService } from "../../shared/_service/api.service";
import { Observable } from "rxjs";
import { Page } from "../../shared/_model/base-models.interface";
import {
	Budget,
	BudgetCreateForm,
	Category,
	CategoryCreateForm, CategoryWithStats,
	SubCategory,
	SubCategoryCreateForm
} from "../_model/budget";
import { ApiPath } from "../../shared/_model/ApiPath";

@Injectable()
export class BudgetService{

	constructor(readonly apiService: ApiService) {
	}

	getBudgets(params?: any): Observable<Page<Budget>> {
		return this.apiService.get<Page<Budget>>(ApiPath.budgets, params);
	}

	getBudget(budgetId: string, params?: any): Observable<Budget> {
		return this.apiService.getFormatted<Budget>(ApiPath.budgetSelect, [budgetId], params)
	}

	postBudget(budgetCreateForm: BudgetCreateForm, params?: any): Observable<Budget> {
		return this.apiService.post<Budget, BudgetCreateForm>(ApiPath.budgets, budgetCreateForm, params)
	}

	archiveBudget(budgetId: string, params?: any): Observable<Budget> {
		return this.apiService.putFormatted(ApiPath.budgetArchive, [budgetId], null, params);
	}

	unArchiveBudget(budgetId: string, params?: any): Observable<Budget> {
		return this.apiService.putFormatted(ApiPath.budgetUnArchive, [budgetId], null, params);
	}

	deleteBudget(budgetId: string, params?: any): Observable<void> {
		return this.apiService.deleteFormatted(ApiPath.budgetSelect, [budgetId], params);
	}

	leaveBudget(budgetId: string, params?: any): Observable<void> {
		return this.apiService.deleteFormatted(ApiPath.budgetLeave, [budgetId], params);
	}

	updateBudget(budgetId: string, updatedBudget: Budget, params?: any): Observable<Budget> {
		return this.apiService.putFormatted(ApiPath.budgetSelect, [budgetId], updatedBudget, params);
	}

	addCategoryToBudget(budgetId: string, categoryForm: CategoryCreateForm, params?: any): Observable<Budget> {
		return this.apiService.postFormatted<Budget, CategoryCreateForm>(ApiPath.budgetCategory, [budgetId], categoryForm, params);
	}

	getCategories(budgetId: string, params?: any): Observable<Category[]> {
		return this.apiService.getFormatted<Category[]>(ApiPath.budgetCategory, [budgetId], params);
	}

	getCategoriesWithStats(budgetId: string, params?: any): Observable<CategoryWithStats[]> {
		return this.apiService.getFormatted<CategoryWithStats[]>(ApiPath.budgetCategoryWithStats, [budgetId], params);
	}

	patchCategory(budgetId: string, updatedCategory: Category, params?: any): Observable<Category> {
		return this.apiService.patchFormatted<Category>(ApiPath.budgetCategory, [budgetId], updatedCategory, params);
	}

	deleteCategory(budgetId: string, categoryId: string, params?: any): Observable<any> {
		return this.apiService.deleteFormatted(ApiPath.budgetCategorySelect, [budgetId, categoryId], params);
	}

	addSubCategoryToCategoryInBudget(budgetId: string, categoryId: string, subCategoryForm: SubCategoryCreateForm, parms?: any): Observable<SubCategory> {
		return this.apiService.postFormatted<SubCategory>(ApiPath.budgetCategorySubCategory, [budgetId, categoryId], subCategoryForm, parms);
	}

	getSubCategories(budgetId: string, categoryId: string, params?: any): Observable<SubCategory[]> {
		return this.apiService.getFormatted<SubCategory[]>(ApiPath.budgetCategorySubCategory, [budgetId, categoryId], params);
	}
}
