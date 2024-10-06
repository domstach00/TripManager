import { Component } from '@angular/core';
import { SearchResultComponent } from "../shared/directives/search-result/search-result.component";
import { Trip } from "../_model/trip";

@Component({
  selector: 'app-trips-table',
  templateUrl: './trips-table.component.html',
  styleUrl: './trips-table.component.scss'
})
export class TripsTableComponent extends SearchResultComponent<Trip> {
	displayedColumns: string[] = ['tripName', 'dayLength', 'summaryCost', 'lastUpdate']


}
