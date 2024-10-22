import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatSidenavModule } from "@angular/material/sidenav";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import {  HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { MatListModule } from "@angular/material/list";
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { RouterOutlet } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { AppRoutingModule } from "./app-routing.module";
import { UserDetailsComponent } from './user-details/user-details.component';
import { MatTableModule } from "@angular/material/table";
import { MatInputModule } from "@angular/material/input";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { NgxGpAutocompleteModule } from "@angular-magic/ngx-gp-autocomplete";
import {
	TripPlanTableAddNewDialogComponent
} from './dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component';
import { MatDialogModule } from "@angular/material/dialog";
import { TripTableAddNewDialogComponent } from './dialog/trip-table-add-new-dialog/trip-table-add-new-dialog.component';
import { MatSortModule } from "@angular/material/sort";
import { ConfirmActionDialogComponent } from './dialog/delete-confirmation-dialog/confirm-action-dialog.component';
import { SelectIconDialogComponent } from './dialog/select-icon-dialog/select-icon-dialog.component';
import { SharedModule } from "./shared/shared.module";
import { AuthModule } from "./auth/auth.module";
import { TripsModule } from "./trips/trips.module";
import { AccountModule } from "./account/account.module";

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		UserDetailsComponent,
		TripPlanTableAddNewDialogComponent,
		TripTableAddNewDialogComponent,
		ConfirmActionDialogComponent,
		SelectIconDialogComponent,
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
		NgxGpAutocompleteModule,
		BrowserModule,
		FormsModule,
		MatDialogModule,
		MatSortModule,
		SharedModule,
		AuthModule,
		TripsModule,
		AccountModule,
	],
	providers: [
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
