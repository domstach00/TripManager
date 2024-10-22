import { NgModule } from "@angular/core";
import { AccountService } from "./account.service";
import { GoogleMapPinService } from "./google-map-pin.service";
import { RouterService } from "./router.service";
import { TripService } from "./trip.service";
import { TripPlanService } from "./trip-plan.service";

@NgModule({
	declarations: [

	],
	providers: [
		TripPlanService,
		TripService,
		RouterService,
		GoogleMapPinService,
		AccountService,
	],
	imports: [
	],
	exports: [
	]
})
export class ServicesModule {

}
