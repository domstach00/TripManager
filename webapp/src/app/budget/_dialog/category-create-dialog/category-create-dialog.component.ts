import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CategoryCreateForm, CategoryDialogData } from "../../_model/budget";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
    selector: 'category-create-dialog',
    templateUrl: './category-create-dialog.component.html',
    styleUrl: './category-create-dialog.component.scss',
    standalone: false
})
export class CategoryCreateDialogComponent {
	categoryForm: FormGroup;
	isEditMode: boolean = false;
	private initialFormValues?: any;

	constructor(
		public dialogRef: MatDialogRef<CategoryCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: CategoryDialogData,
		private fb: FormBuilder
	) {
		this.isEditMode = !!data.category;

		this.categoryForm = this.fb.group({
			name: ['', [Validators.required, Validators.maxLength(100)]],
			allocatedAmount: ['', [
				Validators.min(0),
				Validators.pattern(/^\d+\.?\d{0,2}$/),
				Validators.max(9999999999.99)]
			],
			color: [this.generateRandomColor(), Validators.required]
		});

		if (this.isEditMode && !!data.category.id) {
			this.categoryForm.patchValue({
				name: data.category.name,
				allocatedAmount: data.category.allocatedAmount,
				color: data.category?.color
			})
			this.initialFormValues = { ...this.categoryForm.value };
		}
	}

	hasChanges(): boolean {
		if (!this.isEditMode || !this.initialFormValues) return true;

		const current = this.categoryForm.value;
		const initial = this.initialFormValues;

		const nameChanged: boolean = current.name !== initial?.name;
		const amountChanged: boolean = +current.allocatedAmount !== +initial?.allocatedAmount;
		const colorChanged: boolean = current.color !== initial?.color;

		return nameChanged || amountChanged || colorChanged;
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
			...(this.isEditMode && { id: this.data.category.id }),
			name: this.categoryForm.value.name,
			type: this.data.categoryType,
			allocatedAmount: this.categoryForm.value.allocatedAmount,
			color: this.categoryForm.value.color
		};

		this.dialogRef.close(newCategory);
	}

	onCancel(): void {
		this.dialogRef.close();
	}

	generateRandomColor(): string {
		const letters = '0123456789ABCDEF';
		let color = '#';
		for (let i = 0; i < 6; i++) {
			color += letters[Math.floor(Math.random() * 16)];
		}
		return color;
	}
}
