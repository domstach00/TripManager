import {
	AfterViewChecked,
	AfterViewInit,
	Directive,
	Input,
	OnChanges,
	OnDestroy,
	OnInit,
	SimpleChanges, ViewChild
} from "@angular/core";
import { Account } from "../../../_model/account";
import { MatSort, SortDirection } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Subscription } from "rxjs";

@Directive()
export class SearchResultComponent<T> implements AfterViewChecked, AfterViewInit, OnChanges, OnDestroy, OnInit {
	@Input() currentAccount: Account;
	currentAccountId: string;
	@Input() totalPages: number = -1;
	/**
	 * Use with matSortActive property of the mat-table tag to set this.
	 */
	@Input() sortBy: string;
	/**
	 * Use with matSortDirection property of the mat-table tag to set this.
	 */
	@Input() sortOrder: SortDirection;
	@ViewChild(MatSort) sort: MatSort;

	protected _dataSet: T[];
	dataSource: MatTableDataSource<T>

	protected subscription: Subscription = new Subscription();

	public get dataSet(): T[] {
		return this._dataSet;
	}

	@Input()
	public set dataSet(data: T[]) {
		this._dataSet = data;
	}

	ngAfterViewChecked(): void {
	}

	ngAfterViewInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (!!changes['dataSet']) {
			this.dataSource = new MatTableDataSource<T>(changes['dataSet'].currentValue);
		}
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}

	ngOnInit(): void {
		this.currentAccountId = !!this.currentAccount
			? this.currentAccount.id
			: null;
	}

}
