import { Component, Inject, OnInit } from '@angular/core';
import { Budget, Category, SubCategory } from "../../_model/budget";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { BudgetService } from "../../_service/budget.service";
import { TransactionService } from "../../_service/transaction.service";

@Component({
  selector: 'transaction-create-dialog',
  templateUrl: './transaction-create-dialog.component.html',
  styleUrl: './transaction-create-dialog.component.scss'
})
export class TransactionCreateDialogComponent implements OnInit {
	transactionForm: FormGroup;
	categories: Category[] = [];
	subCategories: SubCategory[] = [];
	loading = false;
	budget: Budget | null = null;

	constructor(
		public dialogRef: MatDialogRef<TransactionCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { budgetId: string },
		private fb: FormBuilder,
		private budgetService: BudgetService,
		private transactionService: TransactionService
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

		this.setupCategoryValidation();
	}

	ngOnInit(): void {
		this.loadBudgetDetails();
	}

	private setupCategoryValidation(): void {
		const subCategoryControl = this.transactionForm.get('subCategoryId');
		this.transactionForm.get('categoryId')?.valueChanges.subscribe(categoryId => {
			if (categoryId) {
				this.loadSubCategories(categoryId);
			} else {
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

	onSubmit(): void {
		if (this.transactionForm.invalid || this.loading || !this.budget) return;

		this.loading = true;
		const transactionData = {
			budgetId: this.budget.id,
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
