import { Component, Input, OnInit } from '@angular/core';
import { Transaction } from "../_model/transaction";
import { SearchableComponent } from "../../shared/directives/search/searchable.component";
import { Page } from "../../shared/_model/base-models.interface";
import { AccountService } from "../../account/_serice/account.service";
import { ActivatedRoute } from "@angular/router";
import { ApiParams } from "../../shared/directives/search/searchable.util";
import { Observable } from "rxjs";
import { TransactionService } from "../_service/transaction.service";

@Component({
  selector: 'transactions-on-category-searchable',
  templateUrl: './transactions-on-category-searchable.component.html',
  styleUrl: './transactions-on-category-searchable.component.scss'
})
export class TransactionsOnCategorySearchableComponent extends SearchableComponent<Transaction, Page<Transaction>> implements OnInit {
	@Input() budgetId!: string;
	@Input() categoryId?: string;

	constructor(
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		private readonly transactionService: TransactionService,
	) {
		super(accountService, activatedRoute);
	}

	doSearch(options: ApiParams): Observable<Page<Transaction>> {
		return this.transactionService.getTransactionsForBudget(this.budgetId, this.categoryId,null,null,null, options);
	}

	override ngOnInit() {
		super.ngOnInit();
		this.prepareQueryParamsAndSearch();
	}
}
