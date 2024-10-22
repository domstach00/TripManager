import { NgModule } from "@angular/core";
import { AccountService } from "./account.service";
import { GoogleMapPinService } from "./google-map-pin.service";

@NgModule({
	declarations: [

	],
	providers: [
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
