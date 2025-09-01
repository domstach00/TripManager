import { Component, Input, OnInit } from '@angular/core';
import { SearchableComponent } from "../../shared/directives/search/searchable.component";
import { Transaction } from "../_model/transaction";
import { Page } from "../../shared/_model/base-models.interface";
import { ApiParams } from "../../shared/directives/search/searchable.util";
import { Observable } from "rxjs";
import { AccountService } from "../../account/_serice/account.service";
import { ActivatedRoute } from "@angular/router";
import { BudgetService } from "../_service/budget.service";
import { MatDialog } from "@angular/material/dialog";
import { TransactionService } from "../_service/transaction.service";
import { CommonModule } from "@angular/common";
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from "@angular/material/sort";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { TranslateModule } from "@ngx-translate/core";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatMenuModule } from "@angular/material/menu";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { FormsModule } from "@angular/forms";
import { TransactionsTableComponent } from "./transactions-table.component";
import { PaginatorComponent } from "../../shared/components/paginator/paginator.component";

@Component({
    selector: 'transactions-searchable',
    templateUrl: './transactions-searchable.component.html',
    styleUrl: './transactions-searchable.component.scss',
    imports: [
        CommonModule,
        MatTableModule,
        MatSortModule,
        MatIconModule,
        MatButtonModule,
        TranslateModule,
        MatTooltipModule,
        MatMenuModule,
        MatFormFieldModule,
        MatInputModule,
        FormsModule,
        TransactionsTableComponent,
        PaginatorComponent
    ]
})
export class TransactionsSearchableComponent extends SearchableComponent<Transaction, Page<Transaction>> implements OnInit {
	@Input() budgetId!: string;
	@Input() categoryId?: string;
	@Input() subCategoryId?: string;
	/**
	 * A flag that, when set to `true`, filters transactions to display only those with unassigned categories.
	 * Defaults to `false`.
	 */
	@Input() excludeCategorized: boolean = false;
	/**
	 * A flag that, when set to `true`, filters transactions to display only those with unassigned subCategories.
	 * Defaults to `false`.
	 */
	@Input() excludeSubCategorized: boolean = false;
	@Input() displayHeaders: boolean = true;


	constructor(
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		readonly budgetService: BudgetService,
		readonly transactionService: TransactionService,
		readonly dialog: MatDialog
	) {
		super(accountService, activatedRoute);
	}

	doSearch(options: ApiParams): Observable<Page<Transaction>> {
		return this.transactionService.getTransactionsForBudget(this.budgetId, this.categoryId, this.subCategoryId, this.excludeCategorized, this.excludeSubCategorized, options);
	}


}
