import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BehaviorSubject, ReplaySubject, Subscription, tap } from "rxjs";
import { Trip } from "../../_model/trip";
import { GoogleMapPin, TripPlan } from "../../_model/trip-plan";
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
export class TripPlanComponent implements OnInit, OnDestroy {
	dataSource$: BehaviorSubject<TripPlan[]> = new BehaviorSubject<TripPlan[]>([])
	obs: ReplaySubject<GoogleMapPin> = new ReplaySubject<GoogleMapPin>();
	showMap: ShowToggle = ShowToggle.ALL;

	subscriptions: Subscription = new Subscription();
	tripId!: string;
	trip?: Trip;

	constructor(
		readonly activatedRoute: ActivatedRoute,
		readonly tripPlanService: TripPlanService
	) {
	}

	ngOnInit(): void {
		this.subscriptions.add(
			this.activatedRoute.params.subscribe(params => {
				this.tripId = params['tripId']
			}))
	}

	refreshData() {
		this.tripPlanService.getTripPlans(this.tripId).pipe(
			tap(tripPlans => this.dataSource$.next(tripPlans.content))
		).subscribe();
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
	}

	changeShowMap(val: ShowToggle) {
		this.showMap = val;
	}


	protected readonly ShowToggle = ShowToggle;
}
