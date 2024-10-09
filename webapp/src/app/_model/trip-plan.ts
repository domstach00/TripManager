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
	displayName?: string;
	address?: string;
	locationLat?: number;
	locationLng?: number;
	iconUrl?: string;
	name?: string;
	vicinity?: string;
	icon?: string;
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
