
export interface PageableModel {
	sort: SortModel;
	pageNumber: number;
	pageSize: number;
	offset: number;
	paged: boolean;
	unpaged: boolean;
}

export interface SortModel {
	sorted: boolean;
	unsorted: boolean;
	empty: boolean;
}

export interface Page<T> {
	content: T[];
	pageable?: PageableModel;
	empty?: boolean;
	first?: boolean;
	last?: boolean;
	totalElements?: number;
	totalPages?: number;
	numberOfElements?: number;
	size?: number;
	sort?: SortModel;
	number?: number;
}
