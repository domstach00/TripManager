import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CategoryCreateForm } from "../../_model/budget";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'category-create-dialog',
  templateUrl: './category-create-dialog.component.html',
  styleUrl: './category-create-dialog.component.scss'
})
export class CategoryCreateDialogComponent {
	categoryForm: FormGroup;

	constructor(
		public dialogRef: MatDialogRef<CategoryCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { budgetId: string, categoryType: 'EXPENSE' | 'INCOME' },
		private fb: FormBuilder
	) {
		this.categoryForm = this.fb.group({
			name: ['', [Validators.required, Validators.maxLength(100)]],
			allocatedAmount: ['', [
				Validators.min(0),
				Validators.pattern(/^\d+\.?\d{0,2}$/),
				Validators.max(9999999999.99)]
			]
		});
	}

	get nameError(): string {
		const control = this.categoryForm.get('name');
		if (control?.hasError('required')) return 'Name is required';
		if (control?.hasError('maxlength')) return 'Max 100 characters';
		return '';
	}

	get amountError(): string {
		const control = this.categoryForm.get('allocatedAmount');
		if (control?.hasError('required')) return 'Amount is required';
		if (control?.hasError('min')) return 'Amount must be positive';
		return '';
	}

	onSubmit(): void {
		if (this.categoryForm.invalid) return;

		const newCategory: CategoryCreateForm = {
			name: this.categoryForm.value.name,
			type: this.data.categoryType,
			allocatedAmount: this.categoryForm.value.allocatedAmount,
		};

		this.dialogRef.close(newCategory);
	}

	onCancel(): void {
		this.dialogRef.close();
	}
}
