import { Component, EventEmitter, Output } from '@angular/core';
import { SearchResultComponent } from "../shared/directives/search-result/search-result.component";
import { Trip } from "../_model/trip";
import { TranslateService } from "@ngx-translate/core";
import { RouterService } from "../_services/router.service";
import { DateUtilService } from "../_services/date-util.service";
import { Account } from "../_model/account";

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrl: './trips-table.component.scss'
})
export class TripsTableComponent extends SearchResultComponent<Trip> {
	displayedColumns: string[] = ['name', 'lastUpdate', 'actions']
	@Output() deleteTripEvent = new EventEmitter<string>();

	constructor(
		protected override readonly translate: TranslateService,
		protected readonly routerService: RouterService,
		protected readonly dateUtilService: DateUtilService,
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

	deleteTrip(tripId: string) {
		this.deleteTripEvent.emit(tripId);
	}

	archiveTrip(tripId: string) {
		// TODO archive trip endpoint
	}

	editTrip(trip: Trip) {
		// TODO allow edit trip items
	}
}
