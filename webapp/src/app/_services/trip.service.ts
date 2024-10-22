import { ApiService } from "../shared/_service/api.service";
import { Trip } from "../_model/trip";
import { ApiPath } from "../shared/_model/ApiPath";
import { Injectable } from "@angular/core";
import { Page } from "../shared/_model/base-models.interface";
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

	public deleteTrip(tripId: string, params?: any): Observable<any> {
		return this.apiService.deleteFormatted(ApiPath.tripSelect, [tripId], params);
	}

	public archiveTrip(tripId: string, params?: any): Observable<Trip> {
		return this.apiService.patchFormatted(ApiPath.tripArchive, [tripId], params);
	}

	public duplicateTrip(tripId: string, params?: any): Observable<Trip> {
		return this.apiService.postFormatted(ApiPath.tripDuplicate, [tripId], params);
	}

	public leaveTripAsMember(tripId: string, params?: any): Observable<any> {
		return this.apiService.deleteFormatted(ApiPath.tripLeave, [tripId], params);
	}

	public isAccountTripOwner(trip: Trip, accountId: string): boolean {
		return trip?.owner?.id === accountId;
	}

	public isAccountTripAdmin(trip: Trip, accountId: string): boolean {
		const isOwner: boolean = this.isAccountTripOwner(trip, accountId);
		if (isOwner) {
			return true;
		}

		return trip?.members?.some(member =>
			member.accountId === accountId && member.memberRole.toUpperCase() === "ADMINISTRATOR");
	}
}
