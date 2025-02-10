import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Budget, Category } from "../../_model/budget";
import { CategoryCreateDialogComponent } from "../../_dialog/category-create-dialog/category-create-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { BudgetService } from "../../_service/budget.service";

@Component({
  selector: 'category-table',
  templateUrl: './category-table.component.html',
  styleUrl: './category-table.component.scss'
})
export class CategoryTableComponent {
	displayedColumns: string[] = ['name', 'allocatedAmount', 'actions'];
	@Input() budget: Budget;
	@Output() refreshEvent: EventEmitter<void> = new EventEmitter<void>();

	constructor(
		readonly dialog: MatDialog,
		readonly budgetService: BudgetService,
	) {
	}

	get expenseCategories(): Category[] {
		return this.budget?.categories?.filter(c => c.type === 'EXPENSE') || [];
	}

	get incomeCategories(): Category[] {
		return this.budget?.categories?.filter(c => c.type === 'INCOME') || [];
	}

	trackByFn(index: number, item: Category): string {
		return item.id;
	}

	openCreateCategoryDialog(budgetType: 'EXPENSE' | 'INCOME'): void {
		const dialogRef = this.dialog.open(CategoryCreateDialogComponent, {
			width: '500px',
			data: { budgetId: this.budget.id, categoryType: budgetType },
		});

		dialogRef.afterClosed().subscribe(newCategory => {
			console.log(newCategory)
			if (newCategory) {
				console.log("in")
				this.budgetService.addCategoryToBudget(this.budget.id, newCategory)
					.subscribe(_ => this.refreshEvent.emit());
			}
		});
	}
}
