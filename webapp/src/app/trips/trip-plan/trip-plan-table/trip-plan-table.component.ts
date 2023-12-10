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
import { TripPlanService } from "../../../_services/trip-plan.service";

interface TripPlanTableItem {
  displayName: string;
  day?: number
  cost?: number;
  mapElementName?: string;
}

@Component({
  selector: 'app-trip-plan-table',
  templateUrl: './trip-plan-table.component.html',
  styleUrls: ['./trip-plan-table.component.scss']
})
export class TripPlanTableComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() inputSearchPlaceHolder = 'search';
  @Input() addValueToTripList?: ReplaySubject<TripPlan>
  @Input() editValue?: ReplaySubject<TripPlan>
  @Input() insertDataSource?: TripPlan[];
  @Input() tripId: string = "";

  @Output()
  addMapPin: EventEmitter<GoogleMapPin> = new EventEmitter();

  @ViewChild('inputField') inputField!: ElementRef;

  autocomplete: google.maps.places.Autocomplete | undefined;
  protected subscriptions: Subscription = new Subscription();

  dataSource: MatTableDataSource<TripPlan> = new MatTableDataSource<TripPlan>();

  test: TripPlanTableItem[] = [{displayName: "abc"}, {displayName: "cba", cost: 1, mapElementName: "ez"}]

  displayedColumns: string[] = ['displayName', 'day', 'cost', 'mapElementName']
  model: any;
  constructor(
    public dialog: MatDialog,
    readonly tripPlanService: TripPlanService,
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.tripPlanService.getTripPlans(this.tripId).subscribe(tripPlans => {
        this.dataSource.data = [...tripPlans];
    }))

    this.subscriptions.add(
      this.addValueToTripList?.subscribe(next => {
        this.dataSource.data.push(next)
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

  doIt(event: google.maps.places.PlaceResult, element: TripPlanTableItem) {
    console.log(event)
    console.log(element)
    console.log(event.formatted_address)
    console.log(event.geometry?.location?.lat())
    console.log(event.geometry?.location?.lng())
    console.log(this.model)
    // console.log(event.target.value)


      let googleMapPin = this.tripPlanService.placeResultToGoogleMapPin(event)

      this.addMapPin.emit(googleMapPin)

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

    dialogRef.afterClosed().subscribe((result) => {
      if (!!result) {
        this.tripPlanService.addTripPlan(result, this.tripId).subscribe(value => {
          this.dataSource.data = [...this.dataSource.data, value]
        });
      }

    });
  }
}
