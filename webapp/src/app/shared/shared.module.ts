import { NgModule } from "@angular/core";
import { TranslateModule } from "@ngx-translate/core";
import { DefaultPageComponent } from "./components/default-page/default-page.component";
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
import { MatListItem, MatListModule, MatNavList } from "@angular/material/list";
import { ApiService } from "./_service/api.service";
import { DateUtilService } from "./_service/date-util.service";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterService } from "./_service/router.service";
import { MatTableModule } from "@angular/material/table";
import { MatInputModule } from "@angular/material/input";
import { MatDialogModule } from "@angular/material/dialog";
import { MatSortModule } from "@angular/material/sort";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatCardModule } from "@angular/material/card";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatSidenavModule } from "@angular/material/sidenav";
import { ConfirmActionDialogComponent } from "./_dialog/delete-confirmation-dialog/confirm-action-dialog.component";
import { AppLoadingComponent } from "./components/app-loader/app-loading.component";

@NgModule({
	declarations: [
		HeaderComponent,
		DefaultPageComponent,
		FooterComponent,
		PaginatorComponent,
		SideBarComponent,
		ConfirmActionDialogComponent,
	],
	imports: [
		MatToolbarModule,
		MatIconModule,
		MatTooltipModule,
		MatDividerModule,
		MatMenuModule,
		CommonModule,
		MatButtonModule,
		MatNavList,
		MatListItem,
		ReactiveFormsModule,
		MatTableModule,
		MatInputModule,
		MatDialogModule,
		MatSortModule,
		MatButtonToggleModule,
		MatCardModule,
		MatGridListModule,
		MatListModule,
		MatSidenavModule,
		FormsModule,
		AppLoadingComponent,
	],
	providers: [
		ApiService,
		DateUtilService,
		RouterService,
	],
	exports: [
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
		ReactiveFormsModule,
		MatTableModule,
		MatInputModule,
		MatDialogModule,
		MatSortModule,
		MatButtonToggleModule,
		MatCardModule,
		MatGridListModule,
		MatListModule,
		MatSidenavModule,
		FormsModule,
		ConfirmActionDialogComponent,
		AppLoadingComponent,
	],
})
export class SharedModule {

}
