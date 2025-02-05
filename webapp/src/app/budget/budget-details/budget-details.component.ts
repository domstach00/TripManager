import { Component, OnInit } from '@angular/core';
import { Budget } from "../_model/budget";
import { ActivatedRoute } from "@angular/router";
import { BudgetService } from "../_service/budget.service";

@Component({
  selector: 'app-budget-details',
  templateUrl: './budget-details.component.html',
  styleUrl: './budget-details.component.scss'
})
export class BudgetDetailsComponent implements OnInit {
	budget!: Budget;
	loading = true;
	error: string | null = null;

	constructor(
		private route: ActivatedRoute,
		private budgetService: BudgetService
	) {}

	ngOnInit(): void {
		this.loadBudgetDetails();
	}

	private loadBudgetDetails(): void {
		const budgetId = this.route.snapshot.paramMap.get('id');
		if (!budgetId) {
			this.error = 'Invalid budget ID';
			return;
		}

		this.budgetService.getBudget(budgetId).subscribe({
			next: (budget) => {
				this.budget = budget;
				this.loading = false;
			},
			error: (err) => {
				this.error = 'Failed to load budget details';
				this.loading = false;
			}
		});
	}
}
