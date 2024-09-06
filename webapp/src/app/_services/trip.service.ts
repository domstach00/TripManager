import { ApiService } from "./api.service";
import { Trip } from "../_model/trip";
import { ApiPath } from "../_model/ApiPath";
import { Injectable } from "@angular/core";

@Injectable()
export class TripService {
	constructor(readonly apiService: ApiService) {
	}

	public getTrips() {
		return this.apiService.get<Trip[]>(ApiPath.trip);
	}

	public postTrip(trip: Trip) {
		return this.apiService.post<Trip, Trip>(ApiPath.trip, trip);
	}
}
