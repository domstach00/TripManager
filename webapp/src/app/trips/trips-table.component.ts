import { Component, EventEmitter, Output } from '@angular/core';
import { SearchResultComponent } from "../shared/directives/search-result/search-result.component";
import { Trip } from "../_model/trip";
import { TranslateService } from "@ngx-translate/core";
import { RouterService } from "../shared/_service/router.service";
import { DateUtilService } from "../shared/_service/date-util.service";
import { Account } from "../_model/account";
import { TripService } from "../_services/trip.service";

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrl: './trips-table.component.scss'
})
export class TripsTableComponent extends SearchResultComponent<Trip> {
	displayedColumns: string[] = ['name', 'lastUpdate', 'actions']
	@Output() refreshEvent = new EventEmitter<string>();

	constructor(
		protected override readonly translate: TranslateService,
		protected readonly routerService: RouterService,
		protected readonly dateUtilService: DateUtilService,
		protected readonly tripService: TripService,
	) {
		super(translate);
	}

	navigateToTrip(tripId: string) {
		this.routerService.navToTrip(tripId);
	}

	formatDate(date: Date): string {
		return this.dateUtilService.formatDate(date);
	}

	formatModifiedBy(account: Account): string {
		if (!account) {
			return '';
		}
		return this.translate.instant('lastModifiedByX', { accountName: account.username });
	}

	isNamePrefixIconVisible(trip: Trip): boolean {
		return !!trip?.isPublic
			|| !!trip?.isClosed
			|| !!trip?.isArchived
			|| !!trip?.isDeleted;
	}

	archiveTrip(tripId: string) {
		this.tripService.archiveTrip(tripId).subscribe(value => {
			this.refreshEvent.emit();
		})
	}

	deleteTrip(tripId: string) {
		this.tripService.deleteTrip(tripId).subscribe(value => {
			this.refreshEvent.emit();
		})
	}

	duplicateTrip(tripId: string) {
		this.tripService.duplicateTrip(tripId).subscribe(duplicatedTrip => {
			this.refreshEvent.emit();
		})
	}

	leaveTripAsMember(tripId: string) {
		this.tripService.leaveTripAsMember(tripId).subscribe( _ => {
			this.refreshEvent.emit();
		})
	}

	isAccountTripAdmin(trip: Trip): boolean {
		return this.tripService.isAccountTripAdmin(trip, this.currentAccountId);
	}

	isAccountTripOwner(trip: Trip): boolean {
		return this.tripService.isAccountTripOwner(trip, this.currentAccountId);
	}

	editTrip(trip: Trip) {
		// TODO allow edit trip items
	}
}
