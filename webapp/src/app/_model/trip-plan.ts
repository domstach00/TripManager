export class TripPlan {
  id: string = "";
  displayName = "";
  day?: number
  cost?: number;
  desc?: string;
  link?: string;
  mapElement?: GoogleMapPin


  getNameToDisplay(): string {
    return !!this.displayName
      ? this.displayName
      : !!this.mapElement?.name
        ? this.mapElement.name
        : ''
  }
}

export interface GoogleMapPin {
  address?: string;
  location?: google.maps.LatLng;
  iconUrl?: string;
  name?: string;
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
