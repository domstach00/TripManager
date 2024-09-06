import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { AuthGuard } from "./guard/auth-guard";
import { TripsComponent } from "./trips/trips.component";
import { TripPlanComponent } from "./trips/trip-plan/trip-plan.component";
import { LogoutComponent } from "./logout/logout.component";


const routes: Routes = [
	{path: 'login', component: LoginFormComponent},
	{path: 'logout', component: LogoutComponent},
	{path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
	{path: 'trips', component: TripsComponent, canActivate: [AuthGuard]},
	{path: 'trips/:tripId', component: TripPlanComponent, canActivate: [AuthGuard]}
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {

}
