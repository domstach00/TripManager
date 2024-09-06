import { Component, EventEmitter, Input, OnInit, Output, } from '@angular/core';
import { TripPlan } from "../../../_model/trip-plan";
import { catchError, Observable, tap, throwError } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
import {
	TripPlanTableAddNewDialogComponent
} from "../../../dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { TripPlanService } from "../../../_services/trip-plan.service";
import {
	ConfirmActionDialogComponent
} from "../../../dialog/delete-confirmation-dialog/confirm-action-dialog.component";
import { IconKeyUrl, SelectIconDialogComponent } from "../../../dialog/select-icon-dialog/select-icon-dialog.component";
import { getIconPath, MapIcon } from "../../../_model/MapPinIcons";

@Component({
	selector: 'app-trip-plan-table',
	templateUrl: './trip-plan-table.component.html',
	styleUrls: ['./trip-plan-table.component.scss']
})
export class TripPlanTableComponent implements OnInit {
	@Input() inputSearchPlaceHolder = 'search';
	@Input() tripId!: string;
	@Input() dataSource!: Observable<TripPlan[]>

	@Output() refreshEvent = new EventEmitter<void>();

	// @ViewChild('inputField') inputField!: ElementRef;

	displayedColumns: string[] = ['displayName', 'day', 'cost', 'mapElementName', 'actions']
	model: any;

	constructor(
		public dialog: MatDialog,
		readonly tripPlanService: TripPlanService,
	) {
	}

	ngOnInit(): void {
		this.refreshData();
	}

	refreshData() {
		this.refreshEvent.emit();
	}

	onSelectIcon(tripPlan: TripPlan) {
		const dialogRef = this.dialog.open(SelectIconDialogComponent, {
			height: '400px',
			width: '600px',
			data: MapIcon,
		});

		dialogRef.afterClosed().subscribe((result: IconKeyUrl) => {
			if (!!result && tripPlan?.mapElement) {
				tripPlan.mapElement.icon = result.key;

				this.tripPlanService.patchTripPlan(tripPlan).pipe(
					tap(() => this.refreshData()),
					catchError(err => {
						console.error('Error while patch', err);
						return throwError(() => new Error(err));
					})).subscribe();
			}
		})
	}

	insertTripPlanDialog(): void {
		const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
			height: '400px',
			width: '600px',
			data: {},
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripPlanService.addTripPlan(result, this.tripId).pipe(
					tap(() => this.refreshData()),
					catchError(err => {
						console.error('Error while post', err);
						return throwError(() => new Error(err));
					})).subscribe();
			}

		});
	}

	editTripPlanDialog(tripPlan: TripPlan) {
		const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
			height: '400px',
			width: '600px',
			data: {...tripPlan},
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripPlanService.patchTripPlan(result).pipe(
					tap(() => this.refreshData()),
					catchError(err => {
						console.error('Error while patch', err);
						return throwError(() => new Error(err));
					})).subscribe();
			}
		})
	}

	deleteItem(tripPlan: TripPlan) {
		const confirmationText = `Do you want to delete item ${tripPlan.displayName}?`
		const dialogRef = this.dialog.open(ConfirmActionDialogComponent, {
			height: '300px',
			width: '600px',
			data: {elementName: tripPlan?.displayName, body: confirmationText, isWarning: true}
		})

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripPlanService.deleteTripPlan(tripPlan.tripId, tripPlan.id).pipe(
					tap(() => this.refreshData())
				).subscribe();
			}
		})
	}

	protected readonly getIconPath = getIconPath;
}
