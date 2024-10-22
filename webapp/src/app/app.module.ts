import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatSidenavModule } from "@angular/material/sidenav";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { MatListModule } from "@angular/material/list";
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { RouterOutlet } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { AppRoutingModule } from "./app-routing.module";
import { UserDetailsComponent } from './user-details/user-details.component';
import { TripsComponent } from './trips/trips.component';
import { MatTableModule } from "@angular/material/table";
import { TripPlanComponent } from './trips/trip-plan/trip-plan.component';
import { TripPlanTableComponent } from './trips/trip-plan/trip-plan-table/trip-plan-table.component';
import { TripPlanMapComponent } from './trips/trip-plan/trip-plan-map/trip-plan-map.component';
import { MatInputModule } from "@angular/material/input";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { GoogleMapsModule } from "@angular/google-maps";
import { NgxGpAutocompleteModule } from "@angular-magic/ngx-gp-autocomplete";
import {
	TripPlanTableAddNewDialogComponent
} from './dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component';
import { MatDialogModule } from "@angular/material/dialog";
import { TripTableAddNewDialogComponent } from './dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component';
import { MatSortModule } from "@angular/material/sort";
import { ConfirmActionDialogComponent } from './dialog/delete-confirmation-dialog/confirm-action-dialog.component';
import { SelectIconDialogComponent } from './dialog/select-icon-dialog/select-icon-dialog.component';
import { Loader } from "@googlemaps/js-api-loader";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { TripsTableComponent } from "./trips/trips-table.component";
import { TripPlanTableSearchableComponent } from "./trips/trip-plan/trip-plan-table/trip-plan-table-searchable.component";
import { SharedModule } from "./shared/shared.module";
import { ServicesModule } from "./_services/services.module";
import { AuthModule } from "./auth/auth.module";

function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http);
}

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		UserDetailsComponent,
		TripsComponent,
		TripsTableComponent,
		TripPlanComponent,
		TripPlanTableComponent,
		TripPlanMapComponent,
		TripPlanTableAddNewDialogComponent,
		TripTableAddNewDialogComponent,
		ConfirmActionDialogComponent,
		SelectIconDialogComponent,
		TripPlanTableSearchableComponent,
	],
	imports: [
		BrowserModule,
		BrowserModule,
		BrowserAnimationsModule,
		MatSidenavModule,
		FormsModule,
		HttpClientModule,
		ToastrModule.forRoot(),
		MatListModule,
		MatGridListModule,
		MatCardModule,
		ReactiveFormsModule,
		RouterOutlet,
		AppRoutingModule,
		MatTableModule,
		MatInputModule,
		MatButtonToggleModule,
		GoogleMapsModule,
		NgxGpAutocompleteModule,
		BrowserModule,
		FormsModule,
		MatDialogModule,
		MatSortModule,
		ServicesModule,
		SharedModule,
		AuthModule,
	],
	providers: [
		{
			provide: Loader,
			useValue: new Loader({
				apiKey: 'YOUR_KEY',
				libraries: ['places',],
			})},
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
