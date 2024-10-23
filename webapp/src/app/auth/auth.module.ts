import { NgModule } from "@angular/core";
import { LoginFormComponent } from "./login-form/login-form.component";
import { RegisterComponent } from "./register/register.component";
import { RegisterSuccessComponent } from "./register/register-success/register-success.component";
import { AuthService } from "./_servive/auth.service";
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { MatIcon } from "@angular/material/icon";
import { TranslateModule } from "@ngx-translate/core";
import { ReactiveFormsModule } from "@angular/forms";
import { AppLoadingModule } from "../shared/components/app-loader/app-loading.module";

@NgModule({
	declarations: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
	],
	imports: [
		RouterModule,
		CommonModule,
		MatIcon,
		TranslateModule,
		ReactiveFormsModule,
		AppLoadingModule,
	],
	providers: [
		AuthService,
	],
	exports: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
	],
})
export class AuthModule {

}
