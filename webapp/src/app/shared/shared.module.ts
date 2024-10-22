import { NgModule } from "@angular/core";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { HttpClient } from "@angular/common/http";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { AppLoadingComponent } from "./components/app-loader/app-loading.component";
import { DefaultPageComponent } from "./components/default-page/default-page.component";
import { ServicesModule } from "../_services/services.module";
import { HeaderComponent } from "./components/header/header.component";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatDividerModule } from "@angular/material/divider";
import { MatMenuModule } from "@angular/material/menu";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { FooterComponent } from "./components/footer/footer.component";
import { PaginatorComponent } from "./components/paginator/paginator.component";
import { SideBarComponent } from "./components/side-bar/side-bar.component";
import { MatListItem, MatNavList } from "@angular/material/list";
import { ApiService } from "../_services/api.service";
import { DateUtilService } from "../_services/date-util.service";

function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http);
}

@NgModule({
	declarations: [
		AppLoadingComponent,
		HeaderComponent,
		DefaultPageComponent,
		FooterComponent,
		PaginatorComponent,
		SideBarComponent,
	],
	imports: [
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient],
			}
		}),
		ServicesModule,
		MatToolbarModule,
		MatIconModule,
		MatTooltipModule,
		MatDividerModule,
		MatMenuModule,
		CommonModule,
		MatButtonModule,
		MatNavList,
		MatListItem,
	],
	providers: [
		ApiService,
		DateUtilService,
	],
	exports: [
		AppLoadingComponent,
		HeaderComponent,
		TranslateModule,
		FooterComponent,
		DefaultPageComponent,
		PaginatorComponent,
		SideBarComponent,
		MatToolbarModule,
		MatIconModule,
		MatTooltipModule,
		MatDividerModule,
		MatMenuModule,
		CommonModule,
		MatButtonModule,
		MatNavList,
		MatListItem,
	],
})
export class SharedModule {

}
