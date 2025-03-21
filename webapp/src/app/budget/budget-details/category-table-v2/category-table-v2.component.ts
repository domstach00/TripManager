import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BudgetService } from "../../_service/budget.service";
import { Category, CategoryDialogData } from "../../_model/budget";
import { CategoryCreateDialogComponent } from "../../_dialog/category-create-dialog/category-create-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import {
	SubcategoryCreateDialogComponent
} from "../../_dialog/subcategory-create-dialog/subcategory-create-dialog.component";
import {
	ConfirmActionDialogComponent
} from "../../../shared/_dialog/delete-confirmation-dialog/confirm-action-dialog.component";

@Component({
  selector: 'category-table-v2',
  templateUrl: './category-table-v2.component.html',
  styleUrl: './category-table-v2.component.scss'
})
export class CategoryTableV2Component implements OnInit {
	@Input() budgetId!: string;
	/**
	 * If categories are provided via `preloadedCategories`,
	 * the initial API request to load categories will be skipped.
	 */
	@Input() preloadedCategories?: Category[] = null;
	@Output() refreshEvent: EventEmitter<void> = new EventEmitter<void>();

	loading: boolean = true;
	categoryList: Category[] = [];
	expandedElements: Category[] = [];

	constructor(
		readonly dialog: MatDialog,
		readonly budgetService: BudgetService,
	) {
	}

	ngOnInit(): void {
		if (!!this.preloadedCategories) {
			this.categoryList = this.preloadedCategories;
			this.loading = false;
		} else {
			this.loadCategories(this.budgetId);
		}
	}

	loadCategories(budgetId: string) {
		this.budgetService.getCategories(budgetId).subscribe( {
			next: categoryList => {
				this.categoryList = categoryList ?? [];
				this.loading = false;
			}, error: err => {
				console.error("Error while loading Categories", err)
				this.loading = false;
			}
		})
	}

	openCreateCategoryDialog(budgetType: 'EXPENSE' | 'INCOME'): void {
		const dialogRef = this.dialog.open(CategoryCreateDialogComponent, {
			width: '500px',
			data: { budgetId: this.budgetId, categoryType: budgetType },
		});

		dialogRef.afterClosed().subscribe(newCategory => {
			if (newCategory) {
				this.budgetService.addCategoryToBudget(this.budgetId, newCategory)
					.subscribe(_ => this.loadCategories(this.budgetId));
			}
		});
	}

	trackByFn(index: number, item: Category): string {
		return item.id;
	}

	editCategory(category: Category) {
		const categoryDialogData: CategoryDialogData = {
			budgetId: this.budgetId,
			category: {
				id: category.id,
				name: category.name,
				allocatedAmount: category.allocatedAmount,
			}
		}

		const dialogRef = this.dialog.open(CategoryCreateDialogComponent, {
			width: '500px',
			data: categoryDialogData,
		})

		dialogRef.afterClosed().subscribe( editedCategory => {
			if (editedCategory) {
				this.budgetService.patchCategory(this.budgetId, editedCategory).subscribe(_ => this.loadCategories(this.budgetId));
			}
		})
	}

	addSubCategory(category: Category) {
		const dialogRef = this.dialog.open(SubcategoryCreateDialogComponent, {
			width: '500px',
			data: { budgetId: this.budgetId, categoryId: category.id },
		});

		dialogRef.afterClosed().subscribe(newSubCategory => {
			if (!!newSubCategory) {
				this.budgetService.addSubCategoryToCategoryInBudget(this.budgetId, category.id, newSubCategory).subscribe(_ => this.loadCategories(this.budgetId))
			}
		})
	}

	deleteCategory(category: Category) {
		const confirmationTextKey: string = `Do you want to delete category ${category.name}?`
		const dialogRef = this.dialog.open(ConfirmActionDialogComponent, {
			height: '300px',
			width: '600px',
			data: { elementName: category.name, body: confirmationTextKey, isWarning: true },
		})

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.budgetService.deleteCategory(this.budgetId, category.id).subscribe(_ => {
					this.loadCategories(this.budgetId);
					this.refreshEvent.emit();
				})
			}
		})
	}

	isExpanded(category: Category): boolean {
		return this.expandedElements.indexOf(category) !== -1;
	}

	toggleExpendedElements(category: Category) {
		const index: number = this.expandedElements.indexOf(category);
		if (index >= 0) {
			this.expandedElements.splice(index, 1);
		} else {
			this.expandedElements.push(category);
		}
	}
}
