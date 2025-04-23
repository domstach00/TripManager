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
import { AppLoadingComponent } from "../shared/components/app-loader/app-loading.component";
import { ActivateAccountComponent } from "./activate-account/activate-account.component";
import { ForgotPasswordComponent } from "./forgot-password/forgot-password.component";

@NgModule({
	declarations: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
		ActivateAccountComponent,
		ForgotPasswordComponent,
	],
	imports: [
		RouterModule,
		CommonModule,
		MatIcon,
		TranslateModule,
		ReactiveFormsModule,
		AppLoadingComponent,
	],
	providers: [
		AuthService,
	],
	exports: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
		ForgotPasswordComponent,
	],
})
export class AuthModule {

}
