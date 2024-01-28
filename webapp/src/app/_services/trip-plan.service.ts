import { GoogleMapPin, TripPlan } from "../_model/trip-plan";
import { ApiService } from "./api.service";
import { ApiPath } from "../_model/ApiPath";
import { Injectable } from "@angular/core";
import LatLng = google.maps.LatLng;

@Injectable()
export class TripPlanService {

  constructor(
    readonly apiService: ApiService
  ) {}

  public addTripPlan(tripPlan: TripPlan, tripId: string) {
    return this.apiService.postFormatted<TripPlan>(ApiPath.tripPlan, [tripId], tripPlan);
  }

  public deleteTripPlan(tripId: string, tripPlanId: string) {
    return this.apiService.deleteFormatted(ApiPath.tripPlanSelect, [tripPlanId])
  }

  public patchTripPlan(tripPlan: TripPlan) {
    return this.apiService.patchFormatted<TripPlan>(ApiPath.tripPlan, [tripPlan.tripId], tripPlan);
  }

  public getTripPlans(tripId: string) {
    return this.apiService.getFormatted<TripPlan[]>(ApiPath.tripPlan, [tripId]);
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
      displayName: assignedPlace.name + (assignedPlace.vicinity && assignedPlace.name != assignedPlace.vicinity ? ' ' + assignedPlace.vicinity : ''),
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
