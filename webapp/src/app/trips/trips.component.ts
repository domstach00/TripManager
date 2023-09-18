import { Component, ViewChild } from '@angular/core';
import { Trip } from "../_model/trip";
import { MatTable } from "@angular/material/table";
import { Router } from "@angular/router";

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: Trip[] = [
  {id: "1", name: 'Hydrogen', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "2", name: 'Helium', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "3", name: 'Lithium', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "4", name: 'Beryllium', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "5", name: 'Boron', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "6", name: 'Carbon', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "7", name: 'Nitrogen', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "8", name: 'Oxygen', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "9", name: 'Fluorine', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
  {id: "10", name: 'Neon', last_update_date: "", creation_date: "", day_length: 0, created_by_user: "", summary_cost: 0},
];

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.scss']
})
export class TripsComponent {
  constructor(readonly router: Router) {
  }

  displayedColumns: string[] = ['tripName', 'dayLength', 'summaryCost', 'lastUpdate'];
  dataSource = [...ELEMENT_DATA];

  @ViewChild(MatTable) table: MatTable<PeriodicElement> | undefined;

  addData() {
    const randomElementIndex = Math.floor(Math.random() * ELEMENT_DATA.length);
    this.dataSource.push(ELEMENT_DATA[randomElementIndex]);
    // this.table?.renderRows();
  }

  removeData() {
    this.dataSource.pop();
    // this.table?.renderRows();
  }

  nav(tripId: string) {
    this.router.navigate([`/trips`, tripId])
  }
}
