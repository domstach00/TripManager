import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { HomeComponent } from "./home/home.component";
import { UserDetailsComponent } from './user-details/user-details.component';
import { SharedModule } from "./shared/shared.module";
import { AuthModule } from "./auth/auth.module";
import { AccountModule } from "./account/account.module";
import { RouterOutlet } from "@angular/router";
import { AppRoutingModule } from "./app-routing.module";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";

function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http);
}

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		UserDetailsComponent,
		PageNotFoundComponent,
	],
	imports: [
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient],
			}
		}),
		BrowserAnimationsModule,
		HttpClientModule,
		ToastrModule.forRoot(),
		AppRoutingModule,
		SharedModule,
		AuthModule,
		AccountModule,
		RouterOutlet,
	],
	providers: [
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
