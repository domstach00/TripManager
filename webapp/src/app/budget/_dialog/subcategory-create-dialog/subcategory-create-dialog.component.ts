import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { SubCategoryCreateForm } from "../../_model/budget";

@Component({
  selector: 'subcategory-create-dialog',
  templateUrl: './subcategory-create-dialog.component.html',
  styleUrl: './subcategory-create-dialog.component.scss'
})
export class SubcategoryCreateDialogComponent {
	subCategoryForm: FormGroup;

	constructor(
		public dialogRef: MatDialogRef<SubcategoryCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { budgetId: string, categoryId: string },
		private fb: FormBuilder
	) {
		this.subCategoryForm = this.fb.group({
			name: ['', [Validators.required, Validators.maxLength(50)]],
		});
	}

	get nameError(): string {
		const control = this.subCategoryForm.get('name');
		if (control?.hasError('required')) return 'Name is required';
		if (control?.hasError('maxlength')) return 'Max 50 characters';
		return '';
	}

	onSubmit(): void {
		if (this.subCategoryForm.invalid) return;

		const newSubCategory: SubCategoryCreateForm = {
			name: this.subCategoryForm.value.name,
		}

		this.dialogRef.close(newSubCategory);
	}

	onCancel(): void {
		this.dialogRef.close();
	}
}
