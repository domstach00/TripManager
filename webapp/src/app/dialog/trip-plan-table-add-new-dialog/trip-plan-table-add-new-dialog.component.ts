import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DialogData } from "../../trips/trip-plan/trip-plan-table/trip-plan-table.component";

@Component({
  selector: 'app-trip-plan-table-add-new-dialog',
  templateUrl: './trip-plan-table-add-new-dialog.component.html',
  styleUrls: ['./trip-plan-table-add-new-dialog.component.scss']
})
export class TripPlanTableAddNewDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<TripPlanTableAddNewDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
