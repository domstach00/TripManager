import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { GoogleMapPin, TripPlan } from "../../_model/trip-plan";
import { Observable } from "rxjs";
import { GoogleMap } from "@angular/google-maps";
import { getMapIconPath } from "../../_model/MapPinIcons";
import { GoogleMapsLoaderService } from "../../_service/google-maps-loader.service";

@Component({
	selector: 'app-trip-plan-map',
	templateUrl: './trip-plan-map.component.html',
	styleUrls: ['./trip-plan-map.component.scss']
})
export class TripPlanMapComponent implements OnInit {
	private zoom = 5;

	@ViewChild('map', {static: true})
	map!: GoogleMap;

	@Input()
	tripId!: string;

	@Input() 
	dataSource$!: Observable<TripPlan[]>;
	
	@Output() 
	refreshEvent = new EventEmitter<void>();

	options?: google.maps.MapOptions;

	constructor(
		private googleMapsLoaderService: GoogleMapsLoaderService,
	) {
	}

	ngOnInit() {
		this.googleMapsLoaderService.load()
			.then(() => {
				this.initializeMap();
			})
			.catch(error => {
				console.error('Failed to load Google Maps API:', error);
			});
	}

	private initializeMap() {
        // Set default options immediately to ensure the map component initializes correctly.
        this.options = {
            center: { lat: 50, lng: 19 }, // Default center of Europe
            zoom: this.zoom,
        };

		this.dataSource$.subscribe(tripPlanList => {
			const googleMapPins = tripPlanList
				.map(value => value.mapElement)
				.filter((optionalGoogleMapPin): optionalGoogleMapPin is GoogleMapPin =>
					optionalGoogleMapPin != null
					&& optionalGoogleMapPin.locationLat != null
					&& optionalGoogleMapPin.locationLng != null);

			if (googleMapPins.length > 0) {
				const latlngPins: google.maps.LatLng[] = this.mapGoogleMapPinsToLatLngs(googleMapPins);

                // Update map options with calculated center based on actual data
				this.options = {
                    ...this.options,
					center: {
						lat: this.calcAverage(latlngPins.map(latlngPin => latlngPin.lat())),
						lng: this.calcAverage(latlngPins.map(latlngPin => latlngPin.lng()))
					}
				};
			}
		});
	}

	mapGoogleMapPinsToLatLngs(googleMapPins: GoogleMapPin[]): google.maps.LatLng[] {
		return googleMapPins.map(pin => this.mapGoogleMapPinToLatLng(pin));
	}

	mapGoogleMapPinToLatLng(googleMapPin: GoogleMapPin): google.maps.LatLng {
		return new google.maps.LatLng({lat: googleMapPin.locationLat ?? 0, lng: googleMapPin.locationLng ?? 0});
	}

	private calcAverage(table: number[]) {
		if (!table || table.length === 0) {
			return 0;
		}
		const sum = table.reduce((a, b) => a + b, 0);
		return sum / table.length;
	}

	protected readonly getIconPath = getMapIconPath;
}
