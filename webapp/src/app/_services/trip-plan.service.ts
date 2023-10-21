import { GoogleMapPin, TripPlan } from "../_model/trip-plan";
import { ApiService } from "./api.service";
import { ApiPath } from "../_model/ApiPath";
import LatLng = google.maps.LatLng;
import { Injectable } from "@angular/core";

@Injectable()
export class TripPlanService {

  constructor(
    readonly apiService: ApiService
  ) {}

  public addTripPlan(tripPlan: TripPlan) {
    return this.apiService.post<TripPlan>(ApiPath.tripPlan, tripPlan);
  }

  public getTripPlans() {
    return this.apiService.get<TripPlan[]>(ApiPath.tripPlan);
  }

  public getTripPlan(tripPlanId: string) {
    return this.apiService.getFormatted(ApiPath.tripPlanSelect, [tripPlanId])
  }

  public placeResultToGoogleMapPin(assignedPlace: google.maps.places.PlaceResult): GoogleMapPin {
    return {
      locationLat: assignedPlace.geometry?.location?.lat(),
      locationLng: assignedPlace.geometry?.location?.lng(),
      name: assignedPlace.name,
      vicinity: assignedPlace.vicinity,
      displayName: assignedPlace.name + " " + assignedPlace.vicinity,
      address: assignedPlace.vicinity,
      iconUrl: assignedPlace.icon
    } as GoogleMapPin
  }

  public markerPositionGenerator(lat?: number, lng?: number): google.maps.LatLng | null{
    return !!lat && !!lng
      ? new LatLng({lat: lat, lng: lng})
      : null
  }
}
