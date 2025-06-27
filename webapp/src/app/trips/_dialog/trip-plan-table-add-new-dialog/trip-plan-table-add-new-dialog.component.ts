import {
	Component,
	Inject,
	OnInit,
	AfterViewInit,
	ViewChild,
	ElementRef
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TripPlan } from '../../_model/trip-plan';
import { TripPlanService } from '../../_service/trip-plan.service';
import { GoogleMapsLoaderService } from '../../_service/google-maps-loader.service';

@Component({
	selector: 'app-trip-plan-table-add-new-dialog',
	templateUrl: './trip-plan-table-add-new-dialog.component.html',
	styleUrls: ['./trip-plan-table-add-new-dialog.component.scss']
})
export class TripPlanTableAddNewDialogComponent implements OnInit, AfterViewInit {
	@ViewChild('autocompleteInput', { static: true })
	autocompleteInput!: ElementRef<HTMLInputElement>;

	// pomocnicze pole do dwukierunkowego wiązania
	searchQuery = '';

	constructor(
		private dialogRef: MatDialogRef<TripPlanTableAddNewDialogComponent>,
		private tripPlanService: TripPlanService,
		@Inject(MAT_DIALOG_DATA) public data: TripPlan,
		private googleMapsLoader: GoogleMapsLoaderService
	) {}

	ngOnInit() {
		// jeśli już istnieje pinezka, pokaz jej nazwę
		this.searchQuery = this.data.mapElement?.displayName || '';
	}

	ngAfterViewInit() {
		// załaduj API Google Maps z Places
		this.googleMapsLoader.load().then(() => {
			const autocomplete = new google.maps.places.Autocomplete(
				this.autocompleteInput.nativeElement,
				{ types: ['geocode'] }
			);

			// po wybraniu miejsca
			autocomplete.addListener('place_changed', () => {
				const place = autocomplete.getPlace();
				this.assignPlace(place);
			});
		});
	}

	assignPlace(place: google.maps.places.PlaceResult) {
		const newPin = this.tripPlanService.placeResultToGoogleMapPin(place);

		if (this.data.mapElement) {
			// Update existing mapElement, preserving its ID
			this.data.mapElement = { ...this.data.mapElement, ...newPin };
		} else {
			// Create a new one if it doesn't exist
			this.data.mapElement = newPin;
		}
		this.searchQuery = this.data.mapElement.displayName;
	}

	clearMapPinValue() {
		this.data.mapElement = undefined;
		this.searchQuery = '';
	}

	onCloseClick(): void {
		this.dialogRef.close();
	}
}
