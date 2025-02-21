import { ApiService } from "../../shared/_service/api.service";
import { Transaction, TransactionBudgetSummary, TransactionCreateForm } from "../_model/transaction";
import { ApiPath } from "../../shared/_model/ApiPath";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../../shared/_model/base-models.interface";

@Injectable()
export class TransactionService {
	constructor(readonly apiService: ApiService) {
	}

	postTransactionForBudget(transactionCreateForm: TransactionCreateForm, params?: any): Observable<Transaction> {
		return this.apiService.post<Transaction, TransactionCreateForm>(ApiPath.transaction, transactionCreateForm, params);
	}

	getTransactionsForBudget(budgetId: string, categoryId?: string, subCategoryId?: string, excludeCategorized?: boolean, excludeSubCategorized?: boolean, params?: any): Observable<Page<Transaction>> {
		const updatedParams = {
			...params,
			...(categoryId !== undefined && { categoryId }),
			...(subCategoryId !== undefined && { subCategoryId }),
			...(excludeCategorized !== undefined && { excludeCategorized }),
			...(excludeSubCategorized !== undefined && { excludeSubCategorized }),
		};

		return this.apiService.getFormatted<Page<Transaction>>(ApiPath.transactionBudgetSelect, [budgetId], updatedParams);
	}

	getTransactionSummaryForGivenBudget(budgetId: string, params?: any): Observable<TransactionBudgetSummary> {
		return this.apiService.getFormatted<TransactionBudgetSummary>(ApiPath.transactionBudgetSelectSummary, [budgetId], params);
	}
}
