import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { AuthGuard } from "./guard/auth-guard";
import { TripsComponent } from "./trips/trips.component";
import { TripPlanComponent } from "./trips/trip-plan/trip-plan.component";


const routes: Routes = [
	{ path: '', redirectTo: '/login', pathMatch: 'full' },
	{ path: '**', redirectTo: '/login'  },
	{ path: 'login', component: LoginFormComponent },
	{ path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'settings', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'trips', component: TripsComponent, canActivate: [AuthGuard], children: [
			{ path: 'trips/:tripId', component: TripPlanComponent, canActivate: [AuthGuard] },
		] },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {

}
