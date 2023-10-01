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
  displayName?: string;
  address?: string;
  locationLat?: number;
  locationLng?: number
  iconUrl?: string;
  name?: string;
  vicinity?: string;
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
