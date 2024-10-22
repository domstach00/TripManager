import { Directive, OnDestroy } from "@angular/core";
import { Page } from "../../_model/base-models.interface";
import { Observable, of, Subject, Subscription, tap } from "rxjs";
import { buildQueryParams } from "../../../_model/api/api.util";


export interface ApiParams {
	q?: string;
	page?: number;
	pageSize?: number;
	/**
	 * sortBy value should be a name of field to sort by
	 * */
	sortBy?: string;
	/**
	 * sortOrder value should be 'asc' or 'desc' or comma-separated list of those values.
	 * */
	sortOrder?: string;
}

@Directive()
export class Searchable<T, TPage extends Page<T> = Page<T>> implements OnDestroy {
	searchPlaceholder = "Search..."

	private subscription: Subscription = new Subscription();
	private _queryParams: ApiParams

	public searchResultObservableChanged$: Subject<Observable<TPage>> = new Subject<Observable<TPage>>();

	_page: number = 0;
	private _pageSize: number = null;
	private _queryParameterArray: object[];
	private _sortBy: string = null;
	private _sortOrder: string = null;
	private _searchTerm: string = null;
	private _searchTermPrefix: string = null;
	private _searchTermSuffix: string = null;
	_searchTermTransformer: (searchTerm: string) => string = (searchTerm: string) => searchTerm;
	private _totalPages: number = 0;
	private _totalElements: number = 0;

	private _searchResultObservable: Observable<TPage>;

	constructor(
		private readonly searchFunction: (apiParams: ApiParams) => Observable<TPage>
	) {
	}

	get first(): boolean {
		return this.page === 0;
	}

	get last(): boolean {
		return this.page === this.totalPages - 1;
	}

	get pageLinksToShow(): number {
		return 10;
	}


	get queryParams(): ApiParams {
		return this._queryParams;
	}


	set queryParams(value: ApiParams) {
		this._queryParams = value;
	}

	get queryParameterArray(): object[] {
		return this._queryParameterArray;
	}


	set queryParameterArray(value: object[]) {
		this._queryParameterArray = value;
		this.onQueryParameterArrayChanged();
	}

	get page(): number {
		return this._page;
	}

	set page(value: number) {
		this._page = value;
	}


	get pageSize(): number {
		return this._pageSize;
	}

	set pageSize(value: number) {
		this._pageSize = value;
	}

	get sortBy(): string {
		return this._sortBy;
	}

	set sortBy(value: string) {
		this._sortBy = value;
	}

	get sortOrder(): string {
		return this._sortOrder;
	}

	set sortOrder(value: string) {
		this._sortOrder = value;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
	}

	get searchTermPrefix(): string {
		return this._searchTermPrefix;
	}

	set searchTermPrefix(value: string) {
		this._searchTermPrefix = value;
	}


	get searchTermSuffix(): string {
		return this._searchTermSuffix;
	}

	set searchTermSuffix(value: string) {
		this._searchTermSuffix = value;
	}

	get totalPages(): number {
		return this._totalPages;
	}

	set totalPages(value: number) {
		this._totalPages = value;
	}

	get totalElements(): number {
		return this._totalElements;
	}

	set totalElements(value: number) {
		this._totalElements = value;
	}

	// TODO : include in future search by text
	onSearchTermChanged(): void {
		this.page = 0;
		this.prepareQueryParamsAndSearch();
	}

	onQueryParameterArrayChanged(): void {
		this.page = 0;
		this.prepareQueryParamsAndSearch()
	}

	prepareQueryParamsAndSearch(pageIndex?: number) {
		this.queryParams = this.prepareQueryParams(pageIndex);
		return this.search();
	}

	prepareQueryParams(pageIndex: number = this._page): ApiParams {
		let cleanSearchTerm: string = this._searchTerm;
		if (cleanSearchTerm == null) {
			cleanSearchTerm = '';
		} else {
			cleanSearchTerm = cleanSearchTerm.trim();
		}

		if (!!cleanSearchTerm) {
			const searchTermPrefix: string = this._searchTermPrefix;
			if (!!searchTermPrefix) {
				cleanSearchTerm = searchTermPrefix + cleanSearchTerm;
			}

			const searchTermSuffix: string = this._searchTermSuffix;
			if (!!searchTermSuffix) {
				cleanSearchTerm = cleanSearchTerm + searchTermSuffix;
			}
		}

		let cleanPageIndex: number = pageIndex;
		if (cleanPageIndex === null) {
			cleanPageIndex = this.page;
		} else if (cleanPageIndex < 0) {
			cleanPageIndex = 0;
		}

		return buildQueryParams(
			this._searchTermTransformer(cleanSearchTerm),
			this._queryParameterArray,
			cleanPageIndex,
			this._pageSize,
			this._sortBy,
			this._sortOrder,
		);
	}

	search(): Observable<TPage> {
		if (this.searchFunction != null) {
			this._searchResultObservable = this.searchFunction(this.queryParams);
		} else {
			this.searchResultObservable = of(null);
		}

		this.searchResultObservableChanged$.next(this.searchResultObservable);
		return this.searchResultObservable;
	}


	set searchResultObservable(value: Observable<TPage>) {
		if (!!value) {
			this._searchResultObservable = value.pipe(
				tap(
					searchResult => {
						if (!!searchResult) {
							if (searchResult.number != null) {
								this.page = searchResult.number;
								this._totalPages = searchResult.totalPages;
							} else {
								this.page = 0;
								this._totalPages = -1;
							}
							this.pageSize = searchResult.size;
							this._totalElements = searchResult.totalElements;
						}
					}
				)
			);
		} else {
			this._searchResultObservable = value
		}
	}

	get searchResultObservable() {
		return this._searchResultObservable;
	}

	ngOnDestroy(): void {
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
		this.searchTerm = null;
		this.searchResultObservable = null;
	}

	goToPage(n: number): void {
		this.prepareQueryParamsAndSearch(n - 1);
	}

	goToFirstPage(): void {
		this.prepareQueryParamsAndSearch(0);
	}

	goToNextPage(): void {
		if (this.hasNextPage()) {
			this.prepareQueryParamsAndSearch(this.page + 1);
		}
	}

	goToPreviousPage(): void {
		if (this.hasPreviousPage()) {
			this.prepareQueryParamsAndSearch(this.page - 1);
		}
	}

	hasNextPage(): boolean {
		return this.page < this.totalPages - 1;
	}

	hasPreviousPage(): boolean {
		return this.page > 0;
	}

	applySort(options: any) {
		this.page = 0;
		this._totalPages = -1;
		this._totalElements = -1

		if (!options.active || !options.direction) {
			this.sortBy = null;
			this.sortOrder = null;
		} else {
			this.sortBy = options.active;
			this.sortOrder = options.direction;
		}
		this.prepareQueryParamsAndSearch();
	}
}
