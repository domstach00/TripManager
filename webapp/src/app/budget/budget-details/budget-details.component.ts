import { catchError, Observable, tap, throwError } from "rxjs";
import { Component, OnInit, ViewChild } from "@angular/core";
import { TransactionsSearchableComponent } from "../transactions-table/transactions-searchable.component";
import { CategoryTableV2Component } from "./category-table-v2/category-table-v2.component";
import { ExpenseDistributionChartComponent } from "../expense-distribution-chart/expense-distribution-chart.component";
import { Budget } from "../_model/budget";
import { TransactionBudgetSummary } from "../_model/transaction";
import {
	TransactionCreateDialogComponent
} from "../_dialog/transaction-create-dialog/transaction-create-dialog.component";
import { ActivatedRoute } from "@angular/router";
import { BudgetService } from "../_service/budget.service";
import { TransactionService } from "../_service/transaction.service";
import { MatDialog } from "@angular/material/dialog";

@Component({
    selector: 'budget-details',
    templateUrl: './budget-details.component.html',
    styleUrl: './budget-details.component.scss',
    standalone: false
})
export class BudgetDetailsComponent implements OnInit {
	@ViewChild('transactions') transactionsSearchable?: TransactionsSearchableComponent;
	@ViewChild('categoryTables') categoryTable?: CategoryTableV2Component;
	@ViewChild('expenseDistributionChartComponent') expenseDistributionChartComponent: ExpenseDistributionChartComponent

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
		this.loadTransactionBudgetSummary().subscribe();
	}

	openCreateTransactionDialog(): void {
		const dialogRef = this.dialog.open(TransactionCreateDialogComponent, {
			width: '400px',
			data: { budgetId: this.budget.id}
		})

		dialogRef.afterClosed().subscribe(value => {
				if (!!value) {
					this.loadTransactionBudgetSummary().subscribe(() => {
						if (!!this.expenseDistributionChartComponent) {
							this.expenseDistributionChartComponent.refreshChart();
							this.onRefreshEventFromCategoryTable();
						}
					});
				} else if (dialogRef.componentRef.instance.isAddedAnyCategoryOrSubcategory) {
					this.onRefreshCategoryTable()
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

	loadTransactionBudgetSummary(): Observable<TransactionBudgetSummary> {
		this.loadingSummary = true;
		return this.transactionService.getTransactionSummaryForGivenBudget(this.budgetId).pipe(
			tap((resultTransactionBudgetSummary: TransactionBudgetSummary) => {
				this.transactionBudgetSummary = resultTransactionBudgetSummary;
				this.loadingSummary = false;
			}),
			catchError((err) => {
				console.error("Error on loading TransactionBudgetSummary: ", err);
				this.loadingSummary = false;
				return throwError(() => err);
			})
		);
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
		if (!!this.expenseDistributionChartComponent) {
			this.expenseDistributionChartComponent.refreshChart();
		}
	}

	onRefreshCategoryTable() {
		if (!!this.categoryTable) {
			this.categoryTable.loadCategories(this.budgetId);
		}
	}

	onCategoriesDataRefreshed(): void {
		if (!!this.expenseDistributionChartComponent) {
			this.expenseDistributionChartComponent.refreshChart();
		}
	}
}
