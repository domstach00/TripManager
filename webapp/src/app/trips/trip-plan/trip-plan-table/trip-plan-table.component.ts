import { Component, EventEmitter, Input, OnInit, Output, } from '@angular/core';
import { TripPlan } from "../../_model/trip-plan";
import { catchError, tap, throwError } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
import {
	TripPlanTableAddNewDialogComponent
} from "../../../dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { TripPlanService } from "../../_service/trip-plan.service";
import {
	ConfirmActionDialogComponent
} from "../../../dialog/delete-confirmation-dialog/confirm-action-dialog.component";
import { IconKeyUrl, SelectIconDialogComponent } from "../../../dialog/select-icon-dialog/select-icon-dialog.component";
import { defaultMapIcon, getMapIconPath, MapIcon } from "../../_model/MapPinIcons";
import { SearchResultComponent } from "../../../shared/directives/search-result/search-result.component";
import { TranslateService } from "@ngx-translate/core";

@Component({
	selector: 'app-trip-plan-table',
	templateUrl: './trip-plan-table.component.html',
	styleUrls: ['./trip-plan-table.component.scss']
})
export class TripPlanTableComponent extends SearchResultComponent<TripPlan> implements OnInit {
	displayedColumns: string[] = ['name', 'day', 'cost', 'mapElementName', 'actions'];
	@Input() tripId: string;
	@Output() refreshEvent = new EventEmitter<void>();

	constructor(
		override readonly translate: TranslateService,
		public dialog: MatDialog,
		readonly tripPlanService: TripPlanService,
	) {
		super(translate);
	}

	override ngOnInit(): void {
		super.ngOnInit();
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
				this.tripPlanService.addTripPlan(result, this.tripId).subscribe(_ => {
					this.refreshEvent.emit();
				});
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
				this.tripPlanService.patchTripPlan(result).subscribe(_ => {
					this.refreshEvent.emit();
				});
			}
		})
	}

	deleteItem(tripPlan: TripPlan) {
		const confirmationText = `Do you want to delete item ${tripPlan.name}?`
		const dialogRef = this.dialog.open(ConfirmActionDialogComponent, {
			height: '300px',
			width: '600px',
			data: {elementName: tripPlan?.name, body: confirmationText, isWarning: true}
		})

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripPlanService.deleteTripPlan(tripPlan.tripId, tripPlan.id).subscribe(_ => {
					this.refreshEvent.emit();
				});
			}
		})
	}

	protected readonly getIconPath = getMapIconPath;
	protected readonly defaultMapIcon = defaultMapIcon;
}
