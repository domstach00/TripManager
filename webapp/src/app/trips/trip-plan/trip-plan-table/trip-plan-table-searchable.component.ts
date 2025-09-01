import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { SearchableComponent } from "../../../shared/directives/search/searchable.component";
import { TripPlan } from "../../_model/trip-plan";
import { Page } from "../../../shared/_model/base-models.interface";
import { ApiParams } from "../../../shared/directives/search/searchable.util";
import { Observable } from "rxjs";
import { AccountService } from "../../../account/_serice/account.service";
import { ActivatedRoute } from "@angular/router";
import { TripPlanService } from "../../_service/trip-plan.service";
import {
	TripPlanTableAddNewDialogComponent
} from "../../_dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { MatDialog } from "@angular/material/dialog";

@Component({
    selector: 'trip-plan-table-searchable',
    templateUrl: './trip-plan-table-searchable.component.html',
    styleUrl: './trip-plan-table-searchable.component.scss',
    standalone: false
})
export class TripPlanTableSearchableComponent extends SearchableComponent<TripPlan, Page<TripPlan>> implements OnInit, OnChanges {
	@Input() tripId: string;
	@Output() refreshEvent = new EventEmitter<void>();

	constructor(
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		private readonly tripPlanService: TripPlanService,
		private readonly dialog: MatDialog,
	) {
		super(accountService, activatedRoute);
	}


	doSearch(options: ApiParams): Observable<Page<TripPlan>> {
		return this.tripPlanService.getTripPlans(this.tripId);
	}

	override ngOnInit() {
		super.ngOnInit();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['tripId'] && changes['tripId'].currentValue) {
			this.refresh();
		}
	}

	insertTripPlanDialog(): void {
		const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
			height: '400px',
			width: '600px',
			data: {tripId: this.tripId},
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripPlanService.addTripPlan(result, this.tripId).subscribe(_ => {
					this.refresh();
				});
			}
		});
	}

	refresh() {
		this.refreshEvent.emit();
		this.prepareQueryParamsAndSearch();
	}

}
