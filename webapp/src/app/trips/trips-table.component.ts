import { Component } from '@angular/core';
import { SearchResultComponent } from "../shared/directives/search-result/search-result.component";
import { Trip } from "../_model/trip";
import { TranslateService } from "@ngx-translate/core";
import { RouterService } from "../_services/router.service";

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrl: './trips-table.component.scss'
})
export class TripsTableComponent extends SearchResultComponent<Trip> {
	displayedColumns: string[] = ['tripName', 'dayLength', 'summaryCost', 'lastUpdate']

	constructor(
		protected override readonly translate: TranslateService,
		protected readonly routerService: RouterService,
	) {
		super(translate);
	}

	navigateToTrip(tripId: string) {
		this.routerService.navToTrip(tripId);
	}

}
