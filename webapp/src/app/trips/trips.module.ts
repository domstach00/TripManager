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
import { TripService } from "./_service/trip.service";
import { TripPlanService } from "./_service/trip-plan.service";
import { GoogleMapPinService } from "./_service/google-map-pin.service";

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
		TripService,
		TripPlanService,
		GoogleMapPinService,
	],
	exports: [
		TripPlanMapComponent,
		TripPlanTableComponent,
		TripPlanTableSearchableComponent,
		TripPlanComponent,
		TripsTableComponent,
		TripsComponent,
	],
	bootstrap: [TripsComponent]
})
export class TripsModule {

}
