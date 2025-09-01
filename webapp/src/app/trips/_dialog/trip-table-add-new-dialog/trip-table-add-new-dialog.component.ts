import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import {
	TripPlanTableAddNewDialogComponent
} from "../trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { Trip } from "../../_model/trip";

@Component({
    selector: 'app-trip-table-add-new-dialog',
    templateUrl: './trip-table-add-new-dialog.component.html',
    styleUrls: ['./trip-table-add-new-dialog.component.scss'],
    standalone: false
})
export class TripTableAddNewDialogComponent {
	constructor(
		readonly dialogRef: MatDialogRef<TripPlanTableAddNewDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: Trip
	) {
	}

	onCloseClick(): void {
		this.dialogRef.close();
	}
}
