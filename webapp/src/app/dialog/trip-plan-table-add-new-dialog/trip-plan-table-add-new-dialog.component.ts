import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { GoogleMapPin, TripPlan } from "../../_model/trip-plan";
import { TripPlanService } from "../../_services/trip-plan.service";

export interface DialogData {
  name: string;
  day: string;
  cost: number;
  mapElementName: GoogleMapPin;
}

@Component({
  selector: 'app-trip-plan-table-add-new-dialog',
  templateUrl: './trip-plan-table-add-new-dialog.component.html',
  styleUrls: ['./trip-plan-table-add-new-dialog.component.scss']
})
export class TripPlanTableAddNewDialogComponent {
  constructor(
    readonly dialogRef: MatDialogRef<TripPlanTableAddNewDialogComponent>,
    readonly tripPlanService: TripPlanService,
    @Inject(MAT_DIALOG_DATA) public data: TripPlan,
  ) {}

  onCloseClick(): void {
    this.dialogRef.close();
  }

  assignPlace(assignedPlace: google.maps.places.PlaceResult) {
    this.data.mapElement = this.tripPlanService.placeResultToGoogleMapPin(assignedPlace)
  }
}
