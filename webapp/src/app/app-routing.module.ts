import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { AuthGuard } from "./guard/auth-guard";
import { TripsComponent } from "./trips/trips.component";
import { TripPlanComponent } from "./trips/trip-plan/trip-plan.component";
import { LogoutComponent } from "./logout/logout.component";


const routes: Routes = [
  {path: 'login', loadChildren: () => LoginFormComponent},
  {path: 'logout',  loadChildren: () =>  LogoutComponent},
  {path: 'home',  loadChildren: () =>  HomeComponent, canActivate: [AuthGuard]},
  {path: 'trips',  loadChildren: () =>  TripsComponent, canActivate: [AuthGuard]},
  {path: 'trips/:tripId',  loadChildren: () =>  TripPlanComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
