import { animate, state, style, transition, trigger } from "@angular/animations";
import { Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { BudgetService } from "../../_service/budget.service";
import { Category, CategoryDialogData, CategoryWithStats } from "../../_model/budget";
import { CategoryCreateDialogComponent } from "../../_dialog/category-create-dialog/category-create-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import {
	SubcategoryCreateDialogComponent
} from "../../_dialog/subcategory-create-dialog/subcategory-create-dialog.component";
import {
	ConfirmActionDialogComponent
} from "../../../shared/_dialog/delete-confirmation-dialog/confirm-action-dialog.component";
import { SubcategoryTableComponent } from "./subcategory-table/subcategory-table.component";
import {TransactionsSearchableComponent} from "../../transactions-table/transactions-searchable.component";
import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { TranslateModule } from "@ngx-translate/core";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatCardModule } from "@angular/material/card";
import { MatMenuModule } from "@angular/material/menu";


@Component({
  selector: 'category-table-v2',
  templateUrl: './category-table-v2.component.html',
  styleUrl: './category-table-v2.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatProgressBarModule,
    TranslateModule,
    MatTooltipModule,
    SubcategoryTableComponent,
    TransactionsSearchableComponent,
    MatCardModule,
    MatMenuModule
  ],
  animations: [
		trigger('detailExpand', [
			state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
			state('expanded', style({ height: '*', visibility: 'visible' })),
			transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
		]),
	],
})
export class CategoryTableV2Component implements OnInit {
	@Input() budgetId!: string;
	/**
	 * If categories are provided via `preloadedCategories`,
	 * the initial API request to load categories will be skipped.
	 */
	@Input() preloadedCategories?: CategoryWithStats[] = null;
	@Output() refreshEvent: EventEmitter<void> = new EventEmitter<void>();
	@Output() categoriesDataRefreshed: EventEmitter<void> = new EventEmitter<void>();
	@ViewChildren(SubcategoryTableComponent) subcategoryTables: QueryList<SubcategoryTableComponent>;
	@ViewChildren(TransactionsSearchableComponent) transactionTables: QueryList<TransactionsSearchableComponent>;

	loading: boolean = true;
	categoryList: CategoryWithStats[] = [];
	expandedElementIds: Set<string> = new Set<string>();

	constructor(
		readonly dialog: MatDialog,
		readonly budgetService: BudgetService,
	) {
	}

	ngOnInit(): void {
		if (!!this.preloadedCategories) {
			this.categoryList = this.preloadedCategories;
			this.loadSpentStats(this.budgetId);
			this.loading = false;
		} else {
			this.loadCategories(this.budgetId);
		}
	}

	loadCategories(budgetId: string) {
		this.budgetService.getCategories(budgetId).subscribe( {
			next: categoryList => {
				this.loading = false;
				// Update existing categories and add new ones
				categoryList.forEach(newCategory => {
					const existingCategoryIndex = this.categoryList.findIndex(c => c.id === newCategory.id);
					if (existingCategoryIndex > -1) {
						this.categoryList[existingCategoryIndex] = newCategory;
					} else {
						this.categoryList.push(newCategory);
					}
				});
				// Remove categories that no longer exist
				this.categoryList = this.categoryList.filter(existingCategory =>
					categoryList.some(newCategory => newCategory.id === existingCategory.id)
				);

				// Now fetch categories with stats and update totalSpentAmount
				this.loadSpentStats(budgetId);

				// Refresh subcategory tables after categories are loaded and updated
				setTimeout(() => {
					this.subcategoryTables.forEach(table => table.refreshSubCategoryList());
				});
				this.categoriesDataRefreshed.emit();

			}, error: err => {
				console.error("Error while loading Categories", err)
				this.loading = false;
			}
		})
	}

	loadSpentStats(budgetId: string) {
		this.budgetService.getCategoriesWithStats(budgetId).subscribe(categoriesWithStats => {
			console.log('Got: ', categoriesWithStats)
			categoriesWithStats.forEach(categoryWithStats => {
				const existingCategory = this.categoryList.find(c => c.id === categoryWithStats.id);
				if (existingCategory) {
					existingCategory.totalSpentAmount = categoryWithStats.totalSpentAmount;
				}
			});
		});
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
				color: category.color,
			}
		}

		const dialogRef = this.dialog.open(CategoryCreateDialogComponent, {
			width: '500px',
			data: categoryDialogData,
		})

		dialogRef.afterClosed().subscribe( editedCategory => {
			if (editedCategory) {
				this.budgetService.patchCategory(this.budgetId, editedCategory).subscribe(_ => {
					this.loadCategories(this.budgetId);
					this.refreshEvent.emit();
				});
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
		return this.expandedElementIds.has(category.id);
	}

	toggleExpendedElements(category: Category) {
		if (this.expandedElementIds.has(category.id)) {
			this.expandedElementIds.delete(category.id);
		} else {
			this.expandedElementIds.add(category.id);
		}
	}

	calculateSpentPercentage(category: CategoryWithStats): number {
		const allocated = parseFloat(category.allocatedAmount || '0');
		const spent = parseFloat(category.totalSpentAmount || '0');

		if (allocated === 0) {
			return 0;
		}

		return (spent / allocated) * 100;
	}

	public refreshTables() {
		this.loadSpentStats(this.budgetId);
		this.subcategoryTables.forEach(table => table.refreshTransactionsTable());
		this.transactionTables.forEach(table => table.prepareQueryParamsAndSearch());
	}

	getBlanderColor(hex: string | undefined): string {
		console.log('Original hex:', hex);
		if (!hex) {
			return 'rgba(224, 224, 224, 0.3)'; // Default blander color if hex is undefined
		}
		let c: any;
		if(/^#([A-Fa-f0-9]{3}){1,2}$/.test(hex)){
			c= hex.substring(1).split('');
			if(c.length===3){
				c= [c[0], c[0], c[1], c[1], c[2], c[2]];
			}
			c= '0x'+c.join('');
			const blanderColor = 'rgba('+[(c>>16)&255, (c>>8)&255, c&255].join(',')+',0.3)';
			console.log('Blander color:', blanderColor);
			return blanderColor;
		}
		console.log('Invalid hex, returning default blander color.');
		return 'rgba(224, 224, 224, 0.3)'; // Fallback for invalid hex
	}
}
