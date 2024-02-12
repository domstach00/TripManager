import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

export interface ConfirmActionData {
  actionName?: string;
  body: string;
}

@Component({
  selector: 'app-confirmation-action-dialog',
  templateUrl: './confirm-action-dialog.component.html',
  styleUrls: ['./confirm-action-dialog.component.scss']
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
}
