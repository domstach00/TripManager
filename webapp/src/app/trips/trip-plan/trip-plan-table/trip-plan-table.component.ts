import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { TripPlan } from "../../../_model/trip-plan";
import { catchError, Observable, tap, throwError } from "rxjs";
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
  @Input() tripId!: string;
  @Input() dataSource!: Observable<TripPlan[]>

  @Output() refreshEvent = new EventEmitter<void>();

  // @ViewChild('inputField') inputField!: ElementRef;

  displayedColumns: string[] = ['displayName', 'day', 'cost', 'mapElementName', 'actions']
  model: any;
  constructor(
    public dialog: MatDialog,
    readonly tripPlanService: TripPlanService,
  ) {}

  ngOnInit(): void {
    this.refreshData();
  }

  refreshData() {
    this.refreshEvent.emit();
  }

  insertTripPlanDialog(): void {
    const dialogRef = this.dialog.open(TripPlanTableAddNewDialogComponent, {
      height: '400px',
      width: '600px',
      data: {}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!!result) {
        this.tripPlanService.addTripPlan(result, this.tripId).pipe(
          tap(() => this.refreshData()),
          catchError(err => {
            console.error('Error while post', err);
            return throwError(() => new Error(err));
          })).subscribe();
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
        this.tripPlanService.patchTripPlan(result).pipe(
          tap(() => this.refreshData()),
          catchError(err => {
            console.error('Error while patch', err);
            return throwError(() => new Error(err));
        })).subscribe();
      }
    })
  }

  deleteItem(tripId: string, tripPlanId: string) {
    this.tripPlanService.deleteTripPlan(tripId, tripPlanId).pipe(
      tap(() => this.refreshData())
    ).subscribe();
  }
}
