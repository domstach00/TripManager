import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'budget-create-dialog',
  templateUrl: './budget-create-dialog.component.html',
  styleUrl: './budget-create-dialog.component.scss'
})
export class BudgetCreateDialogComponent {
	budgetForm: FormGroup;
	newMember: string = '';
	isEditMode: boolean = false;

	constructor(
		public dialogRef: MatDialogRef<BudgetCreateDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		private fb: FormBuilder
	) {
		this.isEditMode = !!this.data.budget;

		this.budgetForm = this.fb.group({
			membersToInvite: [[]],
			newMember: ['', Validators.email],
			name: ['', [Validators.required]],
			description: [''],
			allocatedBudget: [0, [Validators.min(0), Validators.max(9999999999.99)]],
			startDate: [''],
			endDate: ['']
		});

		if (this.isEditMode) {
			this.budgetForm.patchValue({
				name: this.data.budget.name,
				description: this.data.budget.description,
				allocatedBudget: this.data.budget.allocatedBudget,
				startDate: this.data.budget.startDate,
				endDate: this.data.budget.endDate,
				membersToInvite: this.data.budget.membersToInvite || []
			});
		}
	}

	isDateOrderValid(): boolean {
		if (!this.budgetForm.value.startDate || !this.budgetForm.value.endDate) {
			return true;
		}

		const startDate = new Date(this.budgetForm.value.startDate);
		const endDate = new Date(this.budgetForm.value.endDate);
		return startDate <= endDate;
	}

	submit(): void {
		if (this.budgetForm.valid && this.isDateOrderValid()) {
			this.dialogRef.close(this.budgetForm.value);
		}
	}

	close(): void {
		this.dialogRef.close();
	}

	addMember(): void {
		const newMember = this.budgetForm.get('newMember')?.value;
		if (newMember && this.budgetForm.get('newMember')?.valid) {
			const currentMembers = this.budgetForm.get('membersToInvite')?.value || [];
			this.budgetForm.get('membersToInvite')?.setValue([...currentMembers, newMember.trim()]);
			this.budgetForm.get('newMember')?.setValue('');
		}
	}

	removeMember(index: number): void {
		const members = this.budgetForm.get('membersToInvite')?.value;
		members.splice(index, 1);
		this.budgetForm.get('membersToInvite')?.setValue(members);
	}

	isValidEmail(email: string): boolean {
		const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return emailPattern.test(email);
	}
}
