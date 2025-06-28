import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";

export interface ConfirmActionData {
	actionName?: string;
	body: string;
	isWarning?: boolean;
}

@Component({
	selector: 'app-confirmation-action-dialog',
	templateUrl: './confirm-action-dialog.component.html',
	styleUrls: ['./confirm-action-dialog.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class ConfirmActionDialogComponent {
	constructor(
		readonly dialogRef: MatDialogRef<ConfirmActionDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: ConfirmActionData,
	) {
	}

	confirm() {
		this.dialogRef.close(true);
	}

	onCloseClick() {
		this.dialogRef.close();
	}

	getConfirmButtonColor() {
		return this.data.isWarning ? 'btn-danger' : 'btn-primary';
	}
}
