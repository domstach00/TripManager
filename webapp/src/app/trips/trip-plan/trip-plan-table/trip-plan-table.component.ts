import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { GoogleMapPin, TripPlan } from "../../../_model/trip-plan";
import { MatTableDataSource } from "@angular/material/table";
import { ReplaySubject, Subscription } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
import {
  TripPlanTableAddNewDialogComponent
} from "../../../dialog/trip-plan-table-add-new-dialog/trip-plan-table-add-new-dialog.component";
import { TripPlanService } from "../../../_services/trip-plan.service";

@Component({
  selector: 'app-trip-plan-table',
  templateUrl: './trip-plan-table.component.html',
  styleUrls: ['./trip-plan-table.component.scss']
})
export class TripPlanTableComponent implements OnInit {
  @Input() inputSearchPlaceHolder = 'search';
  @Input() addValueToTripList?: ReplaySubject<TripPlan>
  @Input() editValue?: ReplaySubject<TripPlan>
  @Input() insertDataSource?: TripPlan[];
  @Input() tripId: string = "";

  @Output()
  addMapPin: EventEmitter<GoogleMapPin> = new EventEmitter();

  // @ViewChild('inputField') inputField!: ElementRef;

  protected subscriptions: Subscription = new Subscription();

  dataSource: MatTableDataSource<TripPlan> = new MatTableDataSource<TripPlan>();

  displayedColumns: string[] = ['displayName', 'day', 'cost', 'mapElementName', 'actions']
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

  insertTripPlanDialog(): void {
    const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
      height: '400px',
      width: '600px',
      data: {}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!!result) {
        this.tripPlanService.addTripPlan(result, this.tripId).subscribe(value => {
          this.dataSource.data = [...this.dataSource.data, value]
        });
      }

    });
  }

  editTripPlanDialog(tripPlan: TripPlan) {
    const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
      height: '400px',
      width: '600px',
      data: { ...tripPlan }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!!result) {
        this.tripPlanService.patchTripPlan(result).subscribe(tripPlan => {
          console.log("Patched")
        })
      }
    })
  }

  deleteItem(tripId: string, tripPlanId: string) {
    this.tripPlanService.deleteTripPlan(tripId, tripPlanId).subscribe(
      () => console.log("Deleted"),
      error => console.log("not deleted")
    )
  }
}
