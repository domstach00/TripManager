import { Component, Input, OnInit } from '@angular/core';
import { SubCategory } from "../../../_model/budget";
import { BudgetService } from "../../../_service/budget.service";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'subcategory-table',
  templateUrl: './subcategory-table.component.html',
  styleUrl: './subcategory-table.component.scss'
})
export class SubcategoryTableComponent implements OnInit {
	@Input() budgetId!: string;
	@Input() categoryId!: string;
	/**
	 * If subCategories are provided via `preloadedSubCategories`,
	 * the initial API request to load categories will be skipped.
	 */
	@Input() preloadedSubCategories?: SubCategory[] = null;

	subCategoryList: SubCategory[] = [];
	expandedElements: SubCategory[] = [];
	loading: boolean = true;


	constructor(
		readonly dialog: MatDialog,
		readonly budgetService: BudgetService,
	) {
	}

	ngOnInit(): void {
		if (!!this.preloadedSubCategories) {
			this.subCategoryList = this.preloadedSubCategories;
			this.loading = false;
		} else {
			this.loadSubCategories(this.budgetId, this.categoryId);
		}
	}

	loadSubCategories(budgetId: string, categoryId: string) {
		this.budgetService.getSubCategories(budgetId, categoryId).subscribe({
			next: subcategoryList => {
				this.subCategoryList = subcategoryList;
				this.loading = false;
			}, error: err => {
				console.error("Error while loading SubCategories", err)
				this.loading = false;
			}
		})
	}

	isExpanded(category: SubCategory): boolean {
		return this.expandedElements.indexOf(category) !== -1;
	}
	toggleExpendedElements(category: SubCategory) {
		const index: number = this.expandedElements.indexOf(category);
		if (index >= 0) {
			this.expandedElements.splice(index, 1);
		} else {
			this.expandedElements.push(category);
		}
	}

	editSubCategory(subCategory: SubCategory): void {
		// TODO: edit subcategory
	}

	deleteSubCategory(subCategory: SubCategory): void {
		// TODO: delete subcategory
	}
}
