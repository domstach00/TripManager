import { Component, Input, OnInit } from '@angular/core';
import { SearchableComponent } from "../../../shared/directives/search/searchable.component";
import { TripPlan } from "../../../_model/trip-plan";
import { Page } from "../../../_model/base-models.interface";
import { ApiParams } from "../../../shared/directives/search/searchable.util";
import { Observable } from "rxjs";
import { AccountService } from "../../../_services/account.service";
import { ActivatedRoute } from "@angular/router";
import { TripPlanService } from "../../../_services/trip-plan.service";
import {
	TripPlanTableAddNewDialogComponent
} from "../../../dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'trip-plan-table-searchable',
  templateUrl: './trip-plan-table-searchable.component.html',
  styleUrl: './trip-plan-table-searchable.component.scss'
})
export class TripPlanTableSearchableComponent extends SearchableComponent<TripPlan, Page<TripPlan>> implements OnInit {
	@Input() tripId: string;

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
		this.prepareQueryParamsAndSearch();
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
					this.prepareQueryParamsAndSearch();
				});
			}
		});
	}

}