import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import { GoogleMapPin, TripPlan } from "../../../_model/trip-plan";
import { MatTableDataSource } from "@angular/material/table";
import { ReplaySubject, Subscription } from "rxjs";
import LatLng = google.maps.LatLng;
import { MatDialog } from "@angular/material/dialog";
import {
  TripPlanTableAddNewDialogComponent
} from "../../../dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";

interface TripPlanTableItem {
  displayName: string;
  day?: number
  cost?: number;
  mapElementName?: string;
}

export interface DialogData {
  animal: string;
  name: string;
}

@Component({
  selector: 'app-trip-plan-table',
  templateUrl: './trip-plan-table.component.html',
  styleUrls: ['./trip-plan-table.component.scss']
})
export class TripPlanTableComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input()
  inputSearchPlaceHolder = 'search';
  @Input()
  addValueToTripList?: ReplaySubject<TripPlan>
  @Input()
  editValue?: ReplaySubject<TripPlan>

  @Output()
  addMapPin: EventEmitter<GoogleMapPin> = new EventEmitter();

  @ViewChild('inputField') inputField!: ElementRef;

  autocomplete: google.maps.places.Autocomplete | undefined;
  protected subscriptions: Subscription = new Subscription();

  @Input()
  dataSource!: MatTableDataSource<TripPlan>;

  test: TripPlanTableItem[] = [{displayName: "abc"}, {displayName: "cba", cost: 1, mapElementName: "ez"}]

  displayedColumns: string[] = ['displayName', 'day', 'cost', 'mapElementName']
  model: any;
  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {

    this.subscriptions.add(
      this.addValueToTripList?.subscribe(next => {
        this.dataSource?.data.push(next)
      })
    )

  }

  ngAfterViewInit() {
    // this.autocomplete = new google.maps.places.Autocomplete(
    //   this.inputField.nativeElement
    // );

    // this.autocomplete.addListener('place_changed', () => {
    //   const place = this.autocomplete?.getPlace();
    //   const result: GoogleMapPin = {
    //     address: this.inputField.nativeElement.value,
    //     name: place?.name,
    //     location: place?.geometry?.location,
    //     iconUrl: place?.icon,
    //   };
    //   console.log(result)
    //
    //   this.addMapPin.emit(result);
    // });
  }

  mapTripPlanToTripPlanTableItem(tripPlan: TripPlan): TripPlanTableItem {
    return {
      displayName: tripPlan?.displayName,
      day: tripPlan?.day,
      cost: tripPlan?.cost,
      mapElementName: tripPlan?.mapElement?.name
    } as TripPlanTableItem
  }

  ngOnDestroy() {
    // if (this.autocomplete) {
      // google.maps.event.clearInstanceListeners(this.autocomplete);
    // }
  }

  selectAddress(place: google.maps.places.PlaceResult): void {
    // Place object from API
    console.log(place);
  }

  onAddressChange(selectedLocation: any, element: TripPlanTableItem) {
    const googleMapPin: GoogleMapPin = {
      location: this.markerPositionGenerator(selectedLocation.geometry.location.lat(), selectedLocation.geometry.location.lng()),
      name: selectedLocation.name,
      address: selectedLocation.vicinity,
      iconUrl: selectedLocation.icon
    }

    element

  }

  doIt(event: any, element: TripPlanTableItem) {
    console.log(event)
    console.log(element)
    console.log(event.formatted_address)
    console.log(event.geometry.location.lat())
    console.log(event.geometry.location.lng())
    console.log(this.model)
    // console.log(event.target.value)


      let r = {
        location: this.markerPositionGenerator(event.geometry.location.lat(), event.geometry.location.lng()),
        name: event.name + " " + event.vicinity,
        address: event.vicinity,
        iconUrl: event.icon
      } as GoogleMapPin

      this.addMapPin.emit(r)

      // this.autocomplete?.addListener('place_changed', () => {
      //     const place = this.autocomplete?.getPlace();
      //     const result: GoogleMapPin = {
      //         address: this.inputField.nativeElement.value,
      //         name: place?.name,
      //         location: place?.geometry?.location,
      //         iconUrl: place?.icon,
      //     };
      //     console.log(result)
      //     element.mapElementName = place?.name
      //     this.addMapPin.emit(result);
      // });
  }

  markerPositionGenerator(lat: number, lng: number): google.maps.LatLng {
    return new LatLng({lat: lat, lng: lng})
  }


  openDialog(): void {
    const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
      height: '400px',
      width: '600px',
      data: {val: 'a'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
