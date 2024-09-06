import { Component, OnInit, ViewChild } from '@angular/core';
import { Trip } from "../_model/trip";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { TripService } from "../_services/trip.service";
import {
	TripTableAddNewDialogComponent
} from "../dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { Account } from "../_model/account";

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
export class TripsComponent implements OnInit {
	protected subscriptions: Subscription = new Subscription();

	displayedColumns: string[] = ['tripName', 'dayLength', 'summaryCost', 'lastUpdate'];
	dataSource: MatTableDataSource<Trip> = new MatTableDataSource<Trip>(); //= [...ELEMENT_DATA];

	@ViewChild(MatTable) table: MatTable<PeriodicElement> | undefined;

	constructor(
		readonly router: Router,
		readonly tripService: TripService,
		public dialog: MatDialog,
	) {
	}

	ngOnInit(): void {
		this.subscriptions.add(
			this.tripService.getTrips().subscribe(trips => {
				this.dataSource.data = [...trips]
			})
		)
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
		this.router.navigate([`/trips`, tripId])
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
}
