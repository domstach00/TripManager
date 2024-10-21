export interface TripPlan {
	id: string;
	name: string;
	tripId: string
	day?: number
	cost?: number;
	desc?: string;
	link?: string;
	mapElement?: GoogleMapPin;
}

export interface GoogleMapPin {
	displayName?: string; // TODO delete
	address?: string; // TODO delete
	locationLat?: number;
	locationLng?: number;
	iconUrl?: string;
	name?: string;
	vicinity?: string; // TODO delete
	icon?: string;

	streetNumber?: string;
	streetName?: string;
	subLocality?: string;
	locality?: string;
	countryCode?: string;
	postalCode?: string;
}

export interface TripPlanElement {
	elementType: TripPlanElementType;
}

export enum TripPlanElementType {
	ACCOMMODATION,
	TRANSPORT,
	FLIGHT,
	FOOD,
	RESTAURANT,
	OTHER,
	CAR_RENT,
	SIGHTSEEING
}
