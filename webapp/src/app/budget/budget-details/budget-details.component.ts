import { Component, OnInit, ViewChild } from '@angular/core';
import { Budget } from "../_model/budget";
import { ActivatedRoute } from "@angular/router";
import { BudgetService } from "../_service/budget.service";
import { MatDialog } from "@angular/material/dialog";
import {
	TransactionCreateDialogComponent
} from "../_dialog/transaction-create-dialog/transaction-create-dialog.component";
import { TransactionBudgetSummary } from "../_model/transaction";
import { TransactionService } from "../_service/transaction.service";
import { TransactionsSearchableComponent } from "../transactions-table/transactions-searchable.component";
import { CategoryTableV2Component } from "./category-table-v2/category-table-v2.component";

@Component({
  selector: 'budget-details',
  templateUrl: './budget-details.component.html',
  styleUrl: './budget-details.component.scss'
})
export class BudgetDetailsComponent implements OnInit {
	@ViewChild('transactions') transactionsSearchable?: TransactionsSearchableComponent;
	@ViewChild('categoryTables') categoryTable?: CategoryTableV2Component;

	budgetId!: string;
	budget!: Budget;
	loading: boolean = true;
	loadingSummary: boolean = true;
	errorKey: string | null = null;
	transactionBudgetSummary?: TransactionBudgetSummary;

	constructor(
		private route: ActivatedRoute,
		private budgetService: BudgetService,
		private transactionService: TransactionService,
		readonly dialog: MatDialog,
	) {}

	ngOnInit(): void {
		this.budgetId = this.route.snapshot.paramMap.get('id');
		this.loadBudgetDetails();
		this.loadTransactionBudgetSummary();
	}

	openCreateTransactionDialog(): void {
		const dialogRef = this.dialog.open(TransactionCreateDialogComponent, {
			width: '400px',
			data: { budgetId: this.budget.id}
		})

		dialogRef.afterClosed().subscribe(value => {
			if (!!value) {
				this.onRefreshEventFromCategoryTable();
				this.loadTransactionBudgetSummary();
			}
		})
	}

	loadBudgetDetails(): void {
		this.loading = true;
		if (!this.budgetId) {
			this.errorKey = 'budget.details.error.invalid.id';
			return;
		}

		this.budgetService.getBudget(this.budgetId).subscribe({
			next: (budget) => {
				this.budget = budget;
				this.loading = false;
			},
			error: (_) => {
				this.errorKey = 'budget.details.error.loading';
				this.loading = false;
			}
		});
	}

	loadTransactionBudgetSummary(): void {
		this.loadingSummary = true;
		this.transactionService.getTransactionSummaryForGivenBudget(this.budgetId).subscribe({
			next: (resultTransactionBudgetSummary: TransactionBudgetSummary) => {
				this.transactionBudgetSummary = resultTransactionBudgetSummary;
				this.loadingSummary = false;
			},
			error: (err) => {
				console.error("Error on loading TransactionBudgetSummary: ", err);
				this.loadingSummary = false;
			}
		});
	}

	calculateSavings(budgetTotalValue: string, transactionsTotalValue: string): number {
		const budgetValueNumber: number = parseFloat(budgetTotalValue);
		const totalValueNumber: number = parseFloat(transactionsTotalValue);

		if (isNaN(budgetValueNumber) || isNaN(totalValueNumber)) {
			console.error("Cannot convert budget or total to a number.");
			return null;
		}
		return  budgetValueNumber - totalValueNumber;
	}

	onRefreshEventFromCategoryTable() {
		if (!!this.transactionsSearchable) {
			this.transactionsSearchable.prepareQueryParamsAndSearch(this.transactionsSearchable?.page);
		}
		if (!!this.categoryTable) {
			this.categoryTable.refreshTables();
		}
	}
}
