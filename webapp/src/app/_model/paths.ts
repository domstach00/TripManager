export enum Paths {
	login = '/login',
	home = '/home',
	settings = '/settings',
	trips = '/trips',
	trip = trips + '/:tripId'
}

export function mapPath(path: Paths, ids: string[]): string {
	if (!path) {
		return '';
	}
	const pathStr: string = path.valueOf();
	let index = 0;
	return !!ids && ids.length > 0
		? pathStr.replace(/:\w+/g, () => ids[index++])
		: pathStr;
}

export function asRoutePath(path: Paths): string {
	if (!path) {
		return '';
	}
	const pathStr: string = path.valueOf();
	return pathStr.length > 1
		? pathStr.slice(1)
		: pathStr;
}
