import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GoogleMapPin } from "../../../_model/trip-plan";
import { ReplaySubject } from "rxjs";
import { GoogleMap } from "@angular/google-maps";
import LatLng = google.maps.LatLng;

@Component({
  selector: 'app-trip-plan-map',
  templateUrl: './trip-plan-map.component.html',
  styleUrls: ['./trip-plan-map.component.scss']
})
export class TripPlanMapComponent implements OnInit{
  zoom = 5;

  @ViewChild('map', { static: true })
  map!: GoogleMap;

  @Input( { required: true} )
  mapElement!: ReplaySubject<GoogleMapPin>

  markerPositions: google.maps.LatLng[] = [ this.markerPositionGenerator(51.0964311, 17.0348417)];

  options: google.maps.MapOptions = {
    center: {lat: 50, lng: 19},
    zoom: this.zoom,
  }


  ngOnChanges() {
  }

  ngOnInit(): void {
    this.mapElement?.subscribe(value => {
      console.log("Here")
      if (!!value.locationLat && !!value.locationLng)
        this.markerPositions.push(this.markerPositionGenerator(value.locationLat, value.locationLng))
    })


  }



  markerPositionGenerator(lat: number, lng: number): google.maps.LatLng {
    return new LatLng({lat: lat, lng: lng}, true, true)
  }
}
