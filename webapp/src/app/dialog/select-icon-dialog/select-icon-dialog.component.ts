import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";


@Component({
  selector: 'app-select-icon-dialog',
  templateUrl: './select-icon-dialog.component.html',
  styleUrls: ['./select-icon-dialog.component.scss']
})
export class SelectIconDialogComponent {
  constructor(
      public dialogRef: MatDialogRef<SelectIconDialogComponent>,
      @Inject(MAT_DIALOG_DATA) readonly iconEnum: object,
  ) {
  }

  onClose() {
    this.dialogRef.close();
  }

  selectIcon(icon: string) {
    this.dialogRef.close(icon);
  }

  protected readonly Object = Object;
}
