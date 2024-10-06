import { Component, OnInit, ViewChild } from '@angular/core';
import { Trip } from "../_model/trip";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Observable, Subscription } from "rxjs";
import { TripService } from "../_services/trip.service";
import {
	TripTableAddNewDialogComponent
} from "../dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { Account } from "../_model/account";
import { RouterService } from "../_services/router.service";
import { SearchableComponent } from "../shared/directives/search/searchable.component";
import { Page } from "../_model/base-models.interface";
import { ApiParams } from "../shared/directives/search/searchable.util";
import { AccountService } from "../_services/account.service";
import { ActivatedRoute } from "@angular/router";

export interface PeriodicElement {
	name: string;
	position: number;
	weight: number;
	symbol: string;
}


@Component({
	selector: 'app-trips',
	templateUrl: './trips.component.html',
	styleUrls: ['./trips.component.scss']
})
export class TripsComponent extends SearchableComponent<Trip, Page<Trip>> implements OnInit {
	protected subscriptions: Subscription = new Subscription();

	displayedColumns: string[] = ['tripName', 'dayLength', 'summaryCost', 'lastUpdate'];
	dataSource: MatTableDataSource<Trip> = new MatTableDataSource<Trip>(); //= [...ELEMENT_DATA];

	@ViewChild(MatTable) table: MatTable<PeriodicElement> | undefined;

	constructor(
		readonly routerService: RouterService,
		readonly tripService: TripService,
		override readonly accountService: AccountService,
		override readonly activatedRoute: ActivatedRoute,
		public dialog: MatDialog,
	) {
		super(accountService, activatedRoute)
	}

	override ngOnInit(): void {
		super.ngOnInit();
		this.prepareQueryParamsAndSearch()
	}

	formatLastDateTimeUpdate(date: number[], time: number[]): string {
		return date.join("/") + " " + time.slice(0, 2).map(val => val.toString().padStart(2, '0')).join(":")
	}

	formatLastUpdateBy(user: Account): string {
		return "Updated by " + user.username;
	}

	removeData() {
		// this.dataSource.pop();
		// this.table?.renderRows();
	}

	nav(tripId: string) {
		this.routerService.navToTrip(tripId);
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(TripTableAddNewDialogComponent, {
			height: '400px',
			width: '600px',
			data: {val: 'a'}
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (!!result) {
				this.tripService.postTrip(result).subscribe(value => {
					this.dataSource.data = [...this.dataSource.data, value]
				});
			}

		});
	}

	doSearch(options: ApiParams): Observable<Page<Trip>> {
		return this.tripService.getTrips(options);
	}
}
