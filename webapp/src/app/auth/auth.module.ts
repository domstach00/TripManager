import { NgModule } from "@angular/core";
import { LoginFormComponent } from "./login-form/login-form.component";
import { SharedModule } from "../shared/shared.module";
import { RegisterComponent } from "./register/register.component";
import { RegisterSuccessComponent } from "./register/register-success/register-success.component";
import { AuthService } from "./_servive/auth.service";
import { RouterService } from "./_servive/router.service";


@NgModule({
	declarations: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
	],
	imports: [
		SharedModule,
	],
	providers: [
		AuthService,
		RouterService,
	],
	exports: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
	],
})
export class AuthModule {

}
