import { NgModule } from "@angular/core";
import { LoginFormComponent } from "./login-form/login-form.component";
import { SharedModule } from "../shared/shared.module";
import { RegisterComponent } from "./login-form/register/register.component";
import { RegisterSuccessComponent } from "./login-form/register/register-success/register-success.component";

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

	],
	exports: [
		LoginFormComponent,
		RegisterComponent,
		RegisterSuccessComponent,
	],
})
export class AuthModule {

}
