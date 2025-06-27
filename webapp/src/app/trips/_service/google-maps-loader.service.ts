import { Injectable } from "@angular/core";
import { Loader } from "@googlemaps/js-api-loader";
import { environment } from "../../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class GoogleMapsLoaderService {
	private loader: Loader;

	constructor() {
		this.loader = new Loader({
			apiKey: environment.apiKeyGoogleMaps,
			libraries: ["places"],
		});
	}

	load(): Promise<any> {
		return this.loader.load();
	}

	getLoaderInstance(): Loader {
		return this.loader;
	}
}
