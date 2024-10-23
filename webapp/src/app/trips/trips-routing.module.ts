import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import { TripsComponent } from "./trips.component";
import { TripPlanComponent } from "./trip-plan/trip-plan.component";

const tripRoutes: Routes = [
	{ path: '', component: TripsComponent },
	{ path: ':tripId', component: TripPlanComponent },
]

@NgModule({
	imports: [RouterModule.forChild(tripRoutes)],
	exports: [RouterModule]
})
export class TripsRoutingModule {

}
