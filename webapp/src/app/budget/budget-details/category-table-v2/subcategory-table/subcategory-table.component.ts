import {Component, Input, OnInit, ViewChild} from '@angular/core';
import { SubCategory } from "../../../_model/budget";
import { BudgetService } from "../../../_service/budget.service";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import {TransactionsSearchableComponent} from "../../../transactions-table/transactions-searchable.component";
import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { TranslateModule } from "@ngx-translate/core";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatCardModule } from "@angular/material/card";
import { MatMenuModule } from "@angular/material/menu";

@Component({
    selector: 'subcategory-table',
    templateUrl: './subcategory-table.component.html',
    styleUrl: './subcategory-table.component.scss',
    imports: [
        CommonModule,
        MatDialogModule,
        MatIconModule,
        MatButtonModule,
        MatTableModule,
        TranslateModule,
        MatTooltipModule,
        TransactionsSearchableComponent,
        MatCardModule,
        MatMenuModule
    ]
})
export class SubcategoryTableComponent implements OnInit {
	@Input() budgetId!: string;
	@Input() categoryId!: string;
	/**
	 * If subCategories are provided via `preloadedSubCategories`,
	 * the initial API request to load categories will be skipped.
	 */
	@Input() preloadedSubCategories?: SubCategory[] = null;
	@ViewChild('transactionsSearchableComponent') transactionsTable: TransactionsSearchableComponent;

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

	refreshTransactionsTable() {
		if (!!this.transactionsTable) {
			this.transactionsTable.prepareQueryParamsAndSearch(this.transactionsTable?.page);
		}
	}

	public refreshSubCategoryList(): void {
		this.loadSubCategories(this.budgetId, this.categoryId);
	}
}
