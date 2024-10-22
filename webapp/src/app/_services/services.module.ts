import { NgModule } from "@angular/core";
import { AccountService } from "./account.service";
import { GoogleMapPinService } from "./google-map-pin.service";
import { TripPlanService } from "./trip-plan.service";

@NgModule({
	declarations: [

	],
	providers: [
		TripPlanService,
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
