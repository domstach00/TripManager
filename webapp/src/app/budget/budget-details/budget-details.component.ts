import { Component, OnInit } from '@angular/core';
import { Budget } from "../_model/budget";
import { ActivatedRoute } from "@angular/router";
import { BudgetService } from "../_service/budget.service";
import { MatDialog } from "@angular/material/dialog";
import {
	TransactionCreateDialogComponent
} from "../_dialog/transaction-create-dialog/transaction-create-dialog.component";

@Component({
  selector: 'budget-details',
  templateUrl: './budget-details.component.html',
  styleUrl: './budget-details.component.scss'
})
export class BudgetDetailsComponent implements OnInit {
	budget!: Budget;
	loading = true;
	errorKey: string | null = null;

	constructor(
		private route: ActivatedRoute,
		private budgetService: BudgetService,
		readonly dialog: MatDialog,
	) {}

	ngOnInit(): void {
		this.loadBudgetDetails();
	}

	openCreateTransactionDialog(): void {
		const dialogRef = this.dialog.open(TransactionCreateDialogComponent, {
			width: '400px',
			data: { budgetId: this.budget.id}
		})

		dialogRef.afterClosed().subscribe(value => {
			if (!!value) {
				this.loadBudgetDetails();
			}
		})
	}

	private loadBudgetDetails(): void {
		this.loading = true;
		const budgetId = this.route.snapshot.paramMap.get('id');
		if (!budgetId) {
			this.errorKey = 'budget.details.error.invalid.id';
			return;
		}

		this.budgetService.getBudget(budgetId).subscribe({
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
}
