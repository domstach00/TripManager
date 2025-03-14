import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { GoogleMapPin, TripPlan } from "../../_model/trip-plan";
import { BehaviorSubject, Observable } from "rxjs";
import { GoogleMap } from "@angular/google-maps";
import { getMapIconPath } from "../../_model/MapPinIcons";
import { GoogleMapsLoaderService } from "../../_service/google-maps-loader.service";
import { ApiService } from "../../../shared/_service/api.service";
import { ApiPath } from "../../../shared/_model/ApiPath";

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

	pinsSubject = new BehaviorSubject<google.maps.LatLng[]>([]);
	dataSource: Observable<google.maps.LatLng[]> = this.pinsSubject.asObservable();
	@Input() dataSource$!: Observable<TripPlan[]>;
	@Output() refreshEvent = new EventEmitter<void>();

	options?: google.maps.MapOptions;
	// = {
	//   center: {lat: 50, lng: 19},
	//   zoom: this.zoom,
	// }

	constructor(
		private googleMapsLoaderService: GoogleMapsLoaderService,
		private apiService: ApiService,
	) {
	}

	async ngOnInit() {
		this.googleMapsLoaderService.load()
			.then(() => {
				this.initializeMap();
			})
			.catch(error => {
				console.error('Failed to load Google Maps API:', error);
			});

		this.apiService.getFormatted(ApiPath.mapElements, ['6717b6a942f5016247d7d5cc'],).subscribe(value => {
			console.log(value)
		})
	}

	private initializeMap() {
		this.dataSource$.subscribe(tripPlanList => {
			const googleMapPins = [...tripPlanList
				.map(value => value.mapElement)
				.filter((optionalGoogleMapPin): optionalGoogleMapPin is GoogleMapPin =>
					optionalGoogleMapPin != null
					&& !!optionalGoogleMapPin.locationLat
					&& !!optionalGoogleMapPin.locationLng)
			];
			const latlngPins: google.maps.LatLng[] = this.mapGoogleMapPinsToLatLngs(googleMapPins);

			this.options = {
				center: {
					lat: this.calcAverage(latlngPins.map(latlngPin => latlngPin.lat())),
					lng: this.calcAverage(latlngPins.map(latlngPin => latlngPin.lng()))
				},
				zoom: this.zoom

			}

			this.pinsSubject.next(latlngPins);
		})
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

	protected readonly String = String;
	protected readonly getIconPath = getMapIconPath;
}
