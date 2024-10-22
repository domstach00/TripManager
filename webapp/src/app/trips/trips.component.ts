import { Component, OnInit } from '@angular/core';
import { Trip } from "../_model/trip";
import { Observable } from "rxjs";
import { TripService } from "../_services/trip.service";
import {
	TripTableAddNewDialogComponent
} from "../dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { SearchableComponent } from "../shared/directives/search/searchable.component";
import { Page } from "../shared/_model/base-models.interface";
import { ApiParams } from "../shared/directives/search/searchable.util";
import { AccountService } from "../_services/account.service";
import { ActivatedRoute } from "@angular/router";


@Component({
	selector: 'app-trips',
	templateUrl: './trips.component.html',
	styleUrls: ['./trips.component.scss']
})
export class TripsComponent extends SearchableComponent<Trip, Page<Trip>> implements OnInit {

	constructor(
		readonly tripService: TripService,
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		public dialog: MatDialog,
	) {
		super(accountService, activatedRoute)
		this.pageSize = 5;
	}

	override ngOnInit(): void {
		super.ngOnInit();
		this.prepareQueryParamsAndSearch()
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(TripTableAddNewDialogComponent, {
			height: '400px',
			width: '600px',
			data: {}
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripService.postTrip(result).subscribe(value => {
					this.prepareQueryParamsAndSearch();
				});
			}

		});
	}

	doSearch(options: ApiParams): Observable<Page<Trip>> {
		return this.tripService.getTrips(options);
	}
}
