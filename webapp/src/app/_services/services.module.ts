import { NgModule } from "@angular/core";
import { AccountService } from "./account.service";
import { ApiService } from "./api.service";
import { AuthService } from "./auth.service";
import { DateUtilService } from "./date-util.service";
import { GoogleMapPinService } from "./google-map-pin.service";
import { RouterService } from "./router.service";
import { TripService } from "./trip.service";
import { TripPlanService } from "./trip-plan.service";

@NgModule({
	declarations: [

	],
	providers: [
		AuthService,
		TripPlanService,
		TripService,
		RouterService,
		DateUtilService,
		GoogleMapPinService,
		ApiService,
		AccountService,
	],
	imports: [
	],
	exports: [
	]
})
export class ServicesModule {

}
