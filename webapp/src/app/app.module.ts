import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SideBarComponent } from "./shared/components/side-bar/side-bar.component";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatIconModule } from "@angular/material/icon";
import { MatSidenavModule } from "@angular/material/sidenav";
import { LoginFormComponent } from './login-form/login-form.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { AuthService } from "./_services/auth.service";
import { MatButtonModule } from "@angular/material/button";
import { MatListModule } from "@angular/material/list";
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { RouterOutlet } from "@angular/router";
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from "./home/home.component";
import { AppRoutingModule } from "./app-routing.module";
import { FooterComponent } from './footer/footer.component';
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
import { TripPlanService } from "./_services/trip-plan.service";
import { TripService } from "./_services/trip.service";
import { TripTableAddNewDialogComponent } from './dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component';
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatSortModule } from "@angular/material/sort";
import { ConfirmActionDialogComponent } from './dialog/delete-confirmation-dialog/confirm-action-dialog.component';
import { SelectIconDialogComponent } from './dialog/select-icon-dialog/select-icon-dialog.component';
import { DefaultPageComponent } from "./shared/components/default-page/default-page.component";
import { RouterService } from "./_services/router.service";
import { Loader } from "@googlemaps/js-api-loader";
import { PaginatorComponent } from "./shared/components/paginator/paginator.component";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { TripsTableComponent } from "./trips/trips-table.component";
import { DateUtilService } from "./_services/date-util.service";
import { TripPlanTableSearchableComponent } from "./trips/trip-plan/trip-plan-table/trip-plan-table-searchable.component";

function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http);
}

@NgModule({
	declarations: [
		AppComponent,
		SideBarComponent,
		LoginFormComponent,
		HeaderComponent,
		HomeComponent,
		FooterComponent,
		UserDetailsComponent,
		TripsComponent,
		TripsTableComponent,
		TripPlanComponent,
		TripPlanTableComponent,
		TripPlanMapComponent,
		TripPlanTableAddNewDialogComponent,
		TripTableAddNewDialogComponent,
		ConfirmActionDialogComponent,
		DefaultPageComponent,
		SelectIconDialogComponent,
		PaginatorComponent,
		TripPlanTableSearchableComponent,
	],
	imports: [
		BrowserModule,
		BrowserModule,
		BrowserAnimationsModule,
		NgbModule,
		MatToolbarModule,
		MatIconModule,
		MatSidenavModule,
		FormsModule,
		HttpClientModule,
		ToastrModule.forRoot(),
		MatButtonModule,
		MatListModule,
		MatGridListModule,
		MatCardModule,
		MatMenuModule,
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
		MatTooltipModule,
		MatSortModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient],
			}
		})
	],
	providers: [
		AuthService,
		TripPlanService,
		TripService,
		RouterService,
		DateUtilService,
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
