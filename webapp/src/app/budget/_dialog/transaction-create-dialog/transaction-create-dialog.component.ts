import { Component, Inject, OnInit } from '@angular/core';
import { Budget, Category, SubCategory, CategoryType } from "../../_model/budget";
import { FormBuilder, FormGroup, Validators, FormControl } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { BudgetService } from "../../_service/budget.service";
import { TransactionService } from "../../_service/transaction.service";
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'transaction-create-dialog',
  templateUrl: './transaction-create-dialog.component.html',
  styleUrl: './transaction-create-dialog.component.scss'
})
export class TransactionCreateDialogComponent implements OnInit {
	transactionForm: FormGroup;
	categoryControl = new FormControl();
	categories: Category[] = [];
	filteredCategories: Observable<Category[]>;
	subCategories: SubCategory[] = [];
	loading = false;
	budget: Budget | null = null;
	public isAddedAnyCategoryOrSubcategory: boolean = false;

	constructor(
		public dialogRef: MatDialogRef<TransactionCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { budgetId: string },
		private fb: FormBuilder,
		private budgetService: BudgetService,
		private transactionService: TransactionService,
		private snackBar: MatSnackBar
	) {
		this.transactionForm = this.fb.group({
			categoryId: [''],
			subCategoryId: [''],
			description: ['', [Validators.maxLength(255)]],
			amount: ['', [
				Validators.required,
				Validators.min(0),
				Validators.pattern(/^\d+\.?\d{0,2}$/)
			]],
			transactionDate: [new Date(), Validators.required]
		});

		this.filteredCategories = this.categoryControl.valueChanges.pipe(
			startWith(''),
			map(value => this._filter(value || ''))
		);

		this.setupCategoryValidation();
	}

	ngOnInit(): void {
		this.loadBudgetDetails();
	}

	private setupCategoryValidation(): void {
		const subCategoryControl = this.transactionForm.get('subCategoryId');
		this.categoryControl.valueChanges.subscribe(categoryName => {
			const category = this.categories.find(c => c.name === categoryName);
			if (category) {
				this.transactionForm.get('categoryId')?.setValue(category.id);
				this.loadSubCategories(category.id);
			} else {
				this.transactionForm.get('categoryId')?.setValue(null);
				this.subCategories = [];
			}
			subCategoryControl?.updateValueAndValidity();
		});
	}

	private loadBudgetDetails(): void {
		this.loading = true;
		this.budgetService.getBudget(this.data.budgetId).subscribe({
			next: (budget) => {
				this.budget = budget;
				this.categories = budget.categories || [];
				this.filteredCategories = this.categoryControl.valueChanges.pipe(
					startWith(''),
					map(value => this._filter(value || ''))
				);
				this.loading = false;
			},
			error: (err) => {
				console.error('Failed to load budget details', err);
				this.loading = false;
			}
		});
	}

	private loadSubCategories(categoryId: string): void {
		const category = this.categories.find(c => c.id === categoryId);
		this.subCategories = [...(category?.subCategories || [])];

		if (!this.subCategories.length) {
			this.transactionForm.get('subCategoryId')?.setValue(null);
		}
	}

	trackBySubCategory(index: number, subCategory: SubCategory): string {
		return subCategory.id;
	}

	onCategorySelected(event: MatAutocompleteSelectedEvent): void {
		const selectedCategoryName = event.option.value;
		const selectedCategory = this.categories.find(c => c.name === selectedCategoryName);
		if (selectedCategory) {
			this.transactionForm.get('categoryId')?.setValue(selectedCategory.id);
			this.loadSubCategories(selectedCategory.id);
		}
	}

	onCategoryInputBlur(): void {
		const categoryName = this.categoryControl.value;
		if (categoryName) {
			const existingCategory = this.categories.find(c => c.name.toLowerCase() === categoryName.toLowerCase());
			if (!existingCategory) {
				this.addNewCategory(categoryName);
			}
		}
	}

	addNewCategory(categoryName: string): void {
		const trimmedCategoryName = categoryName.trim();
		if (!trimmedCategoryName || !this.budget) {
			return;
		}

		this.loading = true;
		const randomColor = '#' + Math.floor(Math.random() * 16777215).toString(16);

		this.budgetService.addCategoryToBudget(this.budget.id, {
			name: categoryName,
			type: CategoryType.EXPENSE,
			allocatedAmount: '0',
			color: randomColor
		}).subscribe({
			next: (updatedBudget) => {
				const newCategory = updatedBudget.categories?.find(c => c.name === categoryName);
				this.categoryControl.setValue(newCategory.name);
				if (!!newCategory) {
					this.isAddedAnyCategoryOrSubcategory = true;
					this.transactionForm.get('categoryId')?.setValue(newCategory.id);
					this.loadSubCategories(newCategory.id);
					this.loadBudgetDetails();
				} else {
					console.error('Error while finding new category');
				}
			},
			error: (err) => {
				console.error('Failed to add new category', err);
				this.snackBar.open('Failed to add category.', 'Close', { duration: 3000 });
				this.loading = false;
			}
		});
	}

	private _filter(value: string): Category[] {
		const filterValue = value.toLowerCase();
		return this.categories.filter(category => category.name.toLowerCase().includes(filterValue));
	}

	onSubmit(): void {
		if (this.transactionForm.invalid || this.loading || !this.budget) return;

		const categoryId = this.transactionForm.get('categoryId')?.value;
		if (!categoryId) {
			this.snackBar.open('Please select or create a category.', 'Close', { duration: 3000 });
			return;
		}

		this.loading = true;
		const transactionData = {
			budgetId: this.budget.id,
			categoryId: categoryId,
			...this.transactionForm.value,
			amount: parseFloat(this.transactionForm.value.amount),
			transactionDate: this.transactionForm.value.transactionDate.toISOString()
		};

		this.transactionService.postTransactionForBudget(transactionData).subscribe({
			next: (transaction) => {
				this.dialogRef.close(transaction);
				this.loading = false;
			},
			error: (err) => {
				console.error('Transaction creation failed', err);
				this.loading = false;
			}
		});
	}

	get amountError(): string {
		const control = this.transactionForm.get('amount');
		if (control?.hasError('required')) return 'Amount is required';
		if (control?.hasError('min')) return 'Amount must be positive';
		if (control?.hasError('pattern')) return 'Invalid amount format';
		return '';
	}
}
