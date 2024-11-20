import { APP_INITIALIZER, NgModule } from "@angular/core";
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
import {
	TripTableAddNewDialogComponent
} from "./_dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component";
import { FormsModule } from "@angular/forms";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatDialogClose } from "@angular/material/dialog";
import {
	TripPlanTableAddNewDialogComponent
} from "./_dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { NgxGpAutocompleteModule } from "@angular-magic/ngx-gp-autocomplete";
import { SelectIconDialogComponent } from "./_dialog/select-icon-dialog/select-icon-dialog.component";
import { TripsRoutingModule } from "./trips-routing.module";
import { GoogleMapsLoaderService } from "./_service/google-maps-loader.service";

export function initializeGoogleMaps(gmapsLoaderService: GoogleMapsLoaderService) {
	return () => gmapsLoaderService.load();
}

@NgModule({
	declarations: [
		TripPlanMapComponent,
		TripPlanTableComponent,
		TripPlanTableSearchableComponent,
		TripPlanComponent,
		TripsTableComponent,
		TripsComponent,
		TripTableAddNewDialogComponent,
		TripPlanTableAddNewDialogComponent,
		SelectIconDialogComponent,
	],
	imports: [
		GoogleMapsModule,
		SharedModule,
		MatFormField,
		FormsModule,
		MatDialogClose,
		MatLabel,
		NgxGpAutocompleteModule,
		TripsRoutingModule
	],
	providers: [
		{
			provide: APP_INITIALIZER,
			useFactory: initializeGoogleMaps,
			deps: [GoogleMapsLoaderService],
			multi: true,
		},
		{
			provide: Loader,
			useFactory: (gmapsLoaderService: GoogleMapsLoaderService) => gmapsLoaderService.getLoaderInstance,
			deps: [GoogleMapsLoaderService],
		},
		TripService,
		TripPlanService,
		GoogleMapPinService,
		GoogleMapsModule,
	],
	exports: [
		TripPlanMapComponent,
		TripPlanTableComponent,
		TripPlanTableSearchableComponent,
		TripPlanComponent,
		TripsTableComponent,
		TripsComponent,
		TripTableAddNewDialogComponent,
		TripPlanTableAddNewDialogComponent,
		SelectIconDialogComponent,
	],
	bootstrap: [TripsComponent]
})
export class TripsModule {

}
