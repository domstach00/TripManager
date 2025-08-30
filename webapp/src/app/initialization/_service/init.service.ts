import { Injectable } from "@angular/core";
import { ApiService } from "../../shared/_service/api.service";
import { ApiPath } from "../../shared/_model/ApiPath";
import { Observable } from "rxjs";
import { AdminInitRequest } from "../_model/AdminInitRequest";
import { MessageResponse } from "../../shared/_model/base-models.interface";
import { HttpHeaders } from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class InitService {

	constructor(
		private apiService: ApiService,
	) {
	}

	checkEnabled(): Observable<boolean> {
		return this.apiService.get(ApiPath.initializeEnabled);
	}

	createAdmin(adminInitRequest: AdminInitRequest, tokenInitValue: string): Observable<MessageResponse> {
		const headers: HttpHeaders = !!tokenInitValue ? new HttpHeaders({'X-Init-Token': tokenInitValue}) : undefined;
		return this.apiService.post<MessageResponse, AdminInitRequest>(ApiPath.initialize, adminInitRequest, undefined, headers);
	}
}
