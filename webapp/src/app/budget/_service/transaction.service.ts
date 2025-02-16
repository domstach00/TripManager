import { ApiService } from "../../shared/_service/api.service";
import { Transaction, TransactionBudgetSummary, TransactionCreateForm } from "../_model/transaction";
import { ApiPath } from "../../shared/_model/ApiPath";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class TransactionService {
	constructor(readonly apiService: ApiService) {
	}

	postTransaction(transactionCreateForm: TransactionCreateForm, params?: any): Observable<Transaction> {
		return this.apiService.post<Transaction, TransactionCreateForm>(ApiPath.transaction, transactionCreateForm, params);
	}

	getTransactionSummaryForGivenBudget(budgetId: string, params?: any): Observable<TransactionBudgetSummary> {
		console.log("API")
		return this.apiService.getFormatted<TransactionBudgetSummary>(ApiPath.transactionBudgetSummary, [budgetId], params);
	}
}
