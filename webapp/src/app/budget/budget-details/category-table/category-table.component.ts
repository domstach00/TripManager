import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Budget, Category } from "../../_model/budget";
import { CategoryCreateDialogComponent } from "../../_dialog/category-create-dialog/category-create-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { BudgetService } from "../../_service/budget.service";
import { animate, state, style, transition, trigger } from "@angular/animations";
import {
	SubcategoryCreateDialogComponent
} from "../../_dialog/subcategory-create-dialog/subcategory-create-dialog.component";

@Component({
  selector: 'category-table',
  templateUrl: './category-table.component.html',
  styleUrl: './category-table.component.scss',
	animations: [
		trigger('detailExpand', [
			state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
			state('expanded', style({ height: '*', visibility: 'visible' })),
			transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
		]),
	],
})
export class CategoryTableComponent {
	displayedColumns: string[] = ['name', 'allocatedAmount', 'actions'];
	expandedElements: Category[] = [];

	@Input() budget: Budget;
	@Output() refreshEvent: EventEmitter<void> = new EventEmitter<void>();

	constructor(
		readonly dialog: MatDialog,
		readonly budgetService: BudgetService,
	) {}

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
			if (newCategory) {
				this.budgetService.addCategoryToBudget(this.budget.id, newCategory)
					.subscribe(_ => this.refreshEvent.emit());
			}
		});
	}

	toggleRow(row: Category): void {
		const index = this.expandedElements.indexOf(row);
		if (index >= 0) {
			this.expandedElements.splice(index, 1);
		} else {
			this.expandedElements.push(row);
		}
	}

	isExpanded(row: Category): boolean {
		return this.expandedElements.indexOf(row) !== -1;
	}

	// Predicate for expansion row
	isExpansionDetailRow = (index: number, row: Category): boolean => {
		return this.isExpanded(row);
	}

	editCategory(category: Category) {
		// TODO: edit category
	}

	addSubCategory(category: Category) {
		const dialogRef = this.dialog.open(SubcategoryCreateDialogComponent, {
			width: '500px',
			data: { budgetId: this.budget.id, categoryId: category.id },
		});

		dialogRef.afterClosed().subscribe(newSubCategory => {
			if (!!newSubCategory) {
				this.budgetService.addSubCategoryToCategoryInBudget(this.budget.id, category.id, newSubCategory).subscribe(_ => this.refreshEvent.emit())
			}
		})
	}

	deleteCategory(category: Category) {
		// TODO: delete category
	}
}
