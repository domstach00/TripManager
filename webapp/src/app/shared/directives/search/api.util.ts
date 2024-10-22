import { ApiParams } from "./searchable.util";

export const buildQueryParams = (
	searchTerm: string = null,
	queryParameterArray: {}[] = null,
	pageIndex: number = -1,
	pageSize: number = 10,
	sortBy: string = null,
	sortOrder: string = null,
): ApiParams => {
	let queryParams = {};
	if (!!searchTerm) {
		const searchTermParams = { q: searchTerm };
		queryParams = { ...queryParams, ...searchTermParams };
	}

	if (!!queryParameterArray && queryParameterArray.length > 0) {
		queryParameterArray.forEach((queryParameter: {}) => {
			queryParams = { ...queryParams, ...queryParameter };
		});
	}

	if (!!pageIndex && pageIndex > 0) {
		const pageIndexParams = { page: pageIndex };
		queryParams = { ...queryParams, ...pageIndexParams };
	}

	if (!!pageSize && pageSize > 0) {
		const pageSizeParams = { pageSize };
		queryParams = { ...queryParams, ...pageSizeParams };
	}

	if (!!sortOrder) {
		const sortOrderParams = { sortOrder };
		queryParams = { ...queryParams, ...sortOrderParams };
	}

	return queryParams;
}
