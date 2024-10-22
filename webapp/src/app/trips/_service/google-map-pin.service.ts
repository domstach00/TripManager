import { Injectable } from "@angular/core";
import { ApiService } from "../../shared/_service/api.service";
import { GoogleMapPin } from "../_model/trip-plan";
import { ApiPath } from "../../shared/_model/ApiPath";

@Injectable()
export class GoogleMapPinService {

	constructor(
		readonly apiService: ApiService
	) {
	}

	public getGoogleMapPins(tripId: string) {
		return this.apiService.getFormatted<GoogleMapPin[]>(ApiPath.googleMapPin, [tripId]);
	}
}
