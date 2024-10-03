import { ApiService } from "./api.service";
import { Trip } from "../_model/trip";
import { ApiPath } from "../_model/ApiPath";
import { Injectable } from "@angular/core";
import { Page } from "../_model/base-models.interface";
import { Observable } from "rxjs";

@Injectable()
export class TripService {
	constructor(readonly apiService: ApiService) {
	}

	public getTrips(params?: any): Observable<Page<Trip>> {
		return this.apiService.get<Page<Trip>>(ApiPath.trip, params);
	}

	public postTrip(trip: Trip, params?: any) {
		return this.apiService.post<Trip, Trip>(ApiPath.trip, trip, params);
	}
}
