import { ApiService } from "./api.service";
import { Trip } from "../_model/trip";
import { ApiPath } from "../_model/ApiPath";
import { Injectable } from "@angular/core";
import { Page } from "../_model/Page";
import { Observable } from "rxjs";

@Injectable()
export class TripService {
	constructor(readonly apiService: ApiService) {
	}

	public getTrips(): Observable<Page<Trip>> {
		return this.apiService.get<Page<Trip>>(ApiPath.trip);
	}

	public postTrip(trip: Trip) {
		return this.apiService.post<Trip, Trip>(ApiPath.trip, trip);
	}
}
