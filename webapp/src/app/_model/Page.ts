
export interface Page<T> {
	content: T[],
	empty: boolean,
	first: boolean,
	last: boolean,
	totalElements: number,
	totalPages: number,
}
