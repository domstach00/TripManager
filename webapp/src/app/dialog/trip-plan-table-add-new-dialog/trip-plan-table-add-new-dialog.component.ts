import { Component, Inject, Input } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { TripPlan } from "../../_model/trip-plan";
import { TripPlanService } from "../../_services/trip-plan.service";

@Component({
	selector: 'app-trip-plan-table-add-new-dialog',
	templateUrl: './trip-plan-table-add-new-dialog.component.html',
	styleUrls: ['./trip-plan-table-add-new-dialog.component.scss']
})
export class TripPlanTableAddNewDialogComponent {
	@Input() tripId: string;

	constructor(
		readonly dialogRef: MatDialogRef<TripPlanTableAddNewDialogComponent>,
		readonly tripPlanService: TripPlanService,
		@Inject(MAT_DIALOG_DATA) public data: TripPlan,
	) {
	}

	onCloseClick(): void {
		this.dialogRef.close();
	}

	assignPlace(assignedPlace: google.maps.places.PlaceResult) {
		this.data.mapElement = this.tripPlanService.placeResultToGoogleMapPin(assignedPlace)
	}

	clearMapPinValue() {
		this.data.mapElement = undefined;
	}
}
