import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import {  HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { HomeComponent } from "./home/home.component";
import { AppRoutingModule } from "./app-routing.module";
import { UserDetailsComponent } from './user-details/user-details.component';
import { ConfirmActionDialogComponent } from './dialog/delete-confirmation-dialog/confirm-action-dialog.component';
import { SharedModule } from "./shared/shared.module";
import { AuthModule } from "./auth/auth.module";
import { TripsModule } from "./trips/trips.module";
import { AccountModule } from "./account/account.module";

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		UserDetailsComponent,
		ConfirmActionDialogComponent,
	],
	imports: [
		BrowserAnimationsModule,
		HttpClientModule,
		ToastrModule.forRoot(),
		AppRoutingModule,
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
