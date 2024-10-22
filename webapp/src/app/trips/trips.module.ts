import { NgModule } from "@angular/core";
import { TripPlanMapComponent } from "./trip-plan/trip-plan-map/trip-plan-map.component";
import { GoogleMapsModule } from "@angular/google-maps";
import { SharedModule } from "../shared/shared.module";
import { Loader } from "@googlemaps/js-api-loader";
import { TripPlanTableComponent } from "./trip-plan/trip-plan-table/trip-plan-table.component";
import { TripPlanTableSearchableComponent } from "./trip-plan/trip-plan-table/trip-plan-table-searchable.component";
import { TripPlanComponent } from "./trip-plan/trip-plan.component";
import { TripsTableComponent } from "./trips-table.component";
import { TripsComponent } from "./trips.component";

@NgModule({
	declarations: [
		TripPlanMapComponent,
		TripPlanTableComponent,
		TripPlanTableSearchableComponent,
		TripPlanComponent,
		TripsTableComponent,
		TripsComponent,
	],
	imports: [
		GoogleMapsModule,
		SharedModule,
	],
	providers: [
		{
			provide: Loader,
			useValue: new Loader({
				apiKey: 'YOUR_KEY',
				libraries: ['places',],
			})
		},
	],
	exports: [
		TripPlanMapComponent,
		TripPlanTableComponent,
		TripPlanTableSearchableComponent,
		TripPlanComponent,
		TripsTableComponent,
		TripsComponent,
	]
})
export class TripsModule {

}
