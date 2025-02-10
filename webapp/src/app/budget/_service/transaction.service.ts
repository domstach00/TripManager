import { ApiService } from "../../shared/_service/api.service";
import { Transaction, TransactionCreateForm } from "../_model/transaction";
import { ApiPath } from "../../shared/_model/ApiPath";
import { Injectable } from "@angular/core";

@Injectable()
export class TransactionService {
	constructor(readonly apiService: ApiService) {
	}

	postTransaction(transactionCreateForm: TransactionCreateForm, params?: any) {
		return this.apiService.post<Transaction, TransactionCreateForm>(ApiPath.transaction, transactionCreateForm, params);
	}
}
