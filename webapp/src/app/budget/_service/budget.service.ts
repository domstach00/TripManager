import { Injectable } from "@angular/core";
import { ApiService } from "../../shared/_service/api.service";
import { Observable } from "rxjs";
import { Page } from "../../shared/_model/base-models.interface";
import { Budget, BudgetCreateForm } from "../_model/budget";
import { ApiPath } from "../../shared/_model/ApiPath";

@Injectable()
export class BudgetService{

	constructor(readonly apiService: ApiService) {
	}

	getBudgets(params?: any): Observable<Page<Budget>> {
		return this.apiService.get<Page<Budget>>(ApiPath.budgets, params);
	}

	postBudget(budgetCreateForm: BudgetCreateForm, params?: any): Observable<Budget> {
		return this.apiService.post<Budget, BudgetCreateForm>(ApiPath.budgets, budgetCreateForm, params)
	}

	archiveBudget(budgetId: string, params?: any): Observable<Budget> {
		return this.apiService.putFormatted(ApiPath.budgetsArchive, [budgetId], null, params);
	}
}
