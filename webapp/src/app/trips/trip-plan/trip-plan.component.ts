import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { ReplaySubject, Subscription } from "rxjs";
import { Trip } from "../../_model/trip";
import { TripPlan, GoogleMapPin } from "../../_model/trip-plan";
import { TripPlanService } from "../../_services/trip-plan.service";

enum ShowToggle {
  ALL,
  TABLE,
  MAP
}
@Component({
  selector: 'app-trip-plan',
  templateUrl: './trip-plan.component.html',
  styleUrls: ['./trip-plan.component.scss']
})
export class TripPlanComponent implements OnInit, OnDestroy{
  obs: ReplaySubject<GoogleMapPin> = new ReplaySubject<GoogleMapPin>();
  showMap: ShowToggle = ShowToggle.ALL;

  subscriptions: Subscription = new Subscription();
  tripId?: string;
  trip?: Trip;
  tripPlan?: TripPlan

  tripPlans: TripPlan[] = [];
  constructor(
    readonly activatedRoute: ActivatedRoute,
    readonly tripPlanService: TripPlanService
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        this.tripId = params['tripId']
      }))

    // this.tripPlanService.getTripPlans().subscribe(tripPlans => {
    //   this.tripPlans = tripPlans;
    // })
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onOutput(tripMap: GoogleMapPin) {
    console.log("output")
    this.obs?.next(tripMap);
  }

  changeShowMap(val: ShowToggle) {
    this.showMap = val;
  }


  protected readonly ShowToggle = ShowToggle;
}
