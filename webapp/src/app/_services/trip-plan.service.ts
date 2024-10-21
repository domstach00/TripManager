import { GoogleMapPin, TripPlan } from "../_model/trip-plan";
import { ApiService } from "./api.service";
import { ApiPath } from "../_model/ApiPath";
import { Injectable } from "@angular/core";
import LatLng = google.maps.LatLng;
import { Page } from "../_model/base-models.interface";
import { Observable } from "rxjs";

@Injectable({
	providedIn: 'root',
})
export class TripPlanService {

	constructor(
		readonly apiService: ApiService
	) {
	}

	public addTripPlan(tripPlan: TripPlan, tripId: string) {
		return this.apiService.postFormatted<TripPlan>(ApiPath.tripPlan, [tripId], tripPlan);
	}

	public deleteTripPlan(tripId: string, tripPlanId: string) {
		return this.apiService.deleteFormatted(ApiPath.tripPlanSelect, [tripPlanId])
	}

	public patchTripPlan(tripPlan: TripPlan) {
		return this.apiService.patchFormatted<TripPlan>(ApiPath.tripPlan, [tripPlan.tripId], tripPlan);
	}

	public getTripPlans(tripId: string): Observable<Page<TripPlan>> {
		return this.apiService.getFormatted<Page<TripPlan>>(ApiPath.tripPlan, [tripId]);
	}

	public getTripPlan(tripPlanId: string) {
		return this.apiService.getFormatted(ApiPath.tripPlanSelect, [tripPlanId])
	}

	public placeResultToGoogleMapPin(assignedPlace: google.maps.places.PlaceResult): GoogleMapPin {
		const googleMapPin = {
			locationLat: assignedPlace.geometry?.location?.lat(),
			locationLng: assignedPlace.geometry?.location?.lng(),
			name: assignedPlace.name,
			vicinity: assignedPlace.vicinity,
			displayName: assignedPlace.name + (assignedPlace.vicinity && assignedPlace.name != assignedPlace.vicinity ? ' ' + assignedPlace.vicinity : ''),
			address: assignedPlace.vicinity,
			iconUrl: assignedPlace.icon,
		} as GoogleMapPin

		if (!!assignedPlace.address_components) {
			assignedPlace.address_components.forEach(component => {
				const componentType: string = component.types[0];

				switch (componentType) {
					case 'street_number':
						googleMapPin.streetNumber = component.long_name;
						break;
					case 'route':
						googleMapPin.streetName = component.long_name;
						break;
					case 'sublocality_level_1':
						googleMapPin.subLocality = component.long_name;
						break;
					case 'locality':
						googleMapPin.locality = component.long_name;
						break;
					case 'country':
						googleMapPin.countryCode = component.short_name;
						break;
					case 'postal_code':
						googleMapPin.postalCode = component.long_name;
						break;
				}
			});
		}

		return googleMapPin;
	}

	public markerPositionGenerator(lat?: number, lng?: number): google.maps.LatLng | null {
		return !!lat && !!lng
			? new LatLng({lat: lat, lng: lng})
			: null
	}
}
