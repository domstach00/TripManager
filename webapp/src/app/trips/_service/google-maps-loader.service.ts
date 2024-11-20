import { Injectable } from "@angular/core";
import { Loader } from "@googlemaps/js-api-loader";
import { environment } from "../../../environments/environment";

@Injectable({
	providedIn: 'root'
})
export class GoogleMapsLoaderService {
	private loaderInstance: Loader | null = null;
	private isLoading = false;
	private loadPromise: Promise<void> | null = null;

	load(): Promise<void> {
		if (this.loaderInstance) {
			return Promise.resolve();
		}

		if (this.isLoading) {
			return this.loadPromise as Promise<void>;
		}

		this.isLoading = true;
		this.loadPromise = new Promise<void>((resolve, reject) => {
			const script = document.createElement('script');
			const apiKeyGoogleMaps: string = environment.apiKeyGoogleMaps;
			script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKeyGoogleMaps}&libraries=places`;
			script.async = true;
			script.defer = true;

			script.onload = () => {
				this.loaderInstance = new Loader({
					apiKey: apiKeyGoogleMaps,
					libraries: ['places'],
				});
				this.isLoading = false;
				resolve();
			};

			script.onerror = (error) => {
				this.isLoading = false;
				reject(error);
			};

			document.body.appendChild(script);
		});

		return this.loadPromise;
	}

	getLoaderInstance(): Loader {
		if (!this.loaderInstance) {
			const apiKeyGoogleMaps: string = environment.apiKeyGoogleMaps
			this.loaderInstance = new Loader({
				apiKey: apiKeyGoogleMaps,
				libraries: ['places']
			});
		}
		return this.loaderInstance;
	}
}
