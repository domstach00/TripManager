import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { LoginFormComponent } from "./auth/login-form/login-form.component";
import { AuthGuard } from "./guard/auth-guard";
import { TripsComponent } from "./trips/trips.component";
import { TripPlanComponent } from "./trips/trip-plan/trip-plan.component";
import { RegisterComponent } from "./auth/login-form/register/register.component";
import { RegisterSuccessComponent } from "./auth/login-form/register/register-success/register-success.component";


const routes: Routes = [
	{ path: '', redirectTo: '/login', pathMatch: 'full' },
	{ path: 'login', component: LoginFormComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'register-success', component: RegisterSuccessComponent },
	{ path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'settings', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'trips', component: TripsComponent, canActivate: [AuthGuard] },
	{ path: 'trips/:tripId', component: TripPlanComponent, canActivate: [AuthGuard] },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {

}
