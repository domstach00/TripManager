import { Directive, OnDestroy, OnInit } from "@angular/core";
import { Page } from "../../../_model/base-models.interface";
import { ApiParams, Searchable } from "./searchable.util";
import { first, Observable, Subscription, tap } from "rxjs";
import { Account } from "../../../_model/account";
import { AccountService } from "../../../_services/account.service";
import { ActivatedRoute } from "@angular/router";

@Directive()
export abstract class SearchableComponent<T extends { id: any }, TPage extends Page<T> = Page<T>> implements OnInit, OnDestroy {

	protected subscription: Subscription = new Subscription();
	private _searchResult: TPage

	currentAccount$: Observable<Account>;
	currentAccount: Account;


	protected searchable = new Searchable<T, TPage>(
		(params: ApiParams) => {
			const observable$: Observable<TPage> = this.doSearch(params);
			if (!!observable$) {
				return observable$.pipe(
					tap(
						(searchResult: TPage) => {
							this._searchResult = searchResult;
						}
					)
				);
			} else {
				return observable$;
			}
		}
	)

	constructor(
		protected readonly accountService: AccountService,
		protected readonly activatedRoute: ActivatedRoute,
	) {
		this.currentAccount$ = this.accountService.currentAccount.pipe(
			tap(
				account => {
					this.currentAccount = account;
				}
			)
		);
	}

	get firstSortOrder(): 'asc' | 'desc' {
		return this.searchable.sortOrder && this.searchable.sortOrder.toLowerCase().startsWith('desc')
			? 'desc'
			: 'asc';
	}

	get page(): number {
		return this.searchable.page;
	}

	set page(value: number) {
		this.searchable.page = value;
	}

	get pageSize(): number {
		return this.searchable.pageSize;
	}

	set pageSize(value: number) {
		this.searchable.pageSize = value;
	}

	get sortBy(): string {
		return this.searchable.sortBy;
	}

	set sortBy(value: string) {
		this.searchable.sortBy = value;
	}

	get sortOrder(): string {
		return this.searchable.sortOrder;
	}

	set sortOrder(value: string) {
		this.searchable.sortOrder = value;
	}

	get queryParameterArray(): object[] {
		return this.searchable.queryParameterArray;
	}

	set queryParameterArray(value: object[]) {
		this.searchable.queryParameterArray = value;
	}

	get totalPages(): number {
		return this.searchable.totalPages;
	}

	set totalPages(value: number) {
		this.searchable.totalPages = value;
	}

	get totalElements(): number {
		return this.searchable.totalElements;
	}

	set totalElements(value: number) {
		this.searchable.totalElements = value;
	}

	get searchResultObservable$(): Observable<TPage> {
		return this.searchable.searchResultObservable;
	}

	set searchResultObservable$(value: Observable<TPage>) {
		this.searchable.searchResultObservable = value;
	}

	compare<T extends { id: any }>(resultA: T, resultB: T): boolean {
		return !!resultA
			&& !!resultB
			&& resultA['id'] === resultB['id']
	}

	abstract doSearch(options: ApiParams): Observable<TPage>;

	fetchCurrentAccount(): Observable<Account> {
		return this.currentAccount$;
	}

	getSearchResultItem(index: number): T {
		return !!this._searchResult && !!this._searchResult.content
			? this._searchResult.content[index]
			: null;
	}

	protected getSearchTermPrefix(): string {
		return this.searchable.searchTermPrefix;
	}

	protected getSearchTermSuffix(): string {
		return this.searchable.searchTermSuffix;
	}

	goToPage(n: number): void {
		this.searchable.goToPage(n);
		this.prepareQueryParamsAndSearch(n - 1);
	}

	onNext(): void {
		this.searchable.goToNextPage();
		this.prepareQueryParamsAndSearch(this.searchable.page + 1);
	}

	onPrev(): void {
		this.searchable.goToPreviousPage();
		this.prepareQueryParamsAndSearch(this.searchable.page - 1);
	}

	onSort(options: any) {
		this.searchable.applySort(options);
		this.prepareQueryParamsAndSearch();
	}

	handleQueryParameterArrayChanged(queryParameterArray: {}[]): void {
		this.searchable.queryParameterArray = queryParameterArray;
		this.prepareQueryParamsAndSearch();
	}

	prepareQueryParams(pageIndex?: number): ApiParams {
		this.searchable.searchTermPrefix = this.getSearchTermPrefix();
		this.searchable.searchTermSuffix = this.getSearchTermSuffix();
		return this.searchable.prepareQueryParams(pageIndex);
	}

	prepareQueryParamsAndSearch(pageIndex?: number): Observable<TPage> {
		this.searchable.queryParams = this.prepareQueryParams(pageIndex);
		this.searchable.search();
		this.setSearchResultObservable(this.searchable.searchResultObservable);
		return this.searchResultObservable$;
	}

	protected setSearchResultObservable(searchResultObservable$: Observable<TPage>): Observable<TPage> {
		this.searchable.searchResultObservable = searchResultObservable$;
		return this.searchResultObservable$;
	}

	ngOnDestroy(): void {
	}

	ngOnInit(): void {
		this.subscription.add(
			this.activatedRoute.queryParams.pipe(
				first()
			).subscribe(
				paramMap => {
					this.initQueryParameterMap(paramMap);
					this.prepareQueryParamsAndSearch();
				}
			)
		)
	}

	protected initQueryParameterMap(paramMap: any) {
		if (!!paramMap['pageSize']) {
			this.pageSize = paramMap['pageSize'];
		}

		if (!!paramMap['page']) {
			this.page = paramMap['page'];
		}

		if (!!paramMap['sortBy']) {
			this.sortBy = paramMap['sortBy'];
		}

		if (!!paramMap['sortOrder']) {
			this.sortOrder = paramMap['sortOrder'];
		}
	}
}
