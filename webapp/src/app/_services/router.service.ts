import { Router } from "@angular/router";
import { mapPath, Paths } from "../_model/paths";
import { Injectable } from "@angular/core";

@Injectable({
	providedIn: 'root',
})
export class RouterService {
	constructor(
		readonly router: Router,
	) {
	}

	navTo(path: Paths, params?: string[]) {
		const finalPath = !!params
			? mapPath(path, params)
			: path.valueOf();
		this.router.navigate([finalPath])
	}

	public navToSettings() {
		this.navTo(Paths.settings);
	}

	public navToHome() {
		this.navTo(Paths.home);
	}

	public navToTrips() {
		this.navTo(Paths.trips);
	}

	public navToTrip(tripId: string) {
		this.navTo(Paths.trip, [tripId]);
	}

	public navToLogin() {
		this.navTo(Paths.login);
	}
}
