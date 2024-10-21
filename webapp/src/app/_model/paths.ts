export enum Paths {
	login = '/login',
	register = '/register',
	registerSuccess = '/register-success',
	home = '/home',
	settings = '/settings',
	trips = '/trips',
	trip = trips + '/:tripId'
}

export function mapPath(path: Paths, ids?: string[]): string {
	if (!path) {
		return '';
	}
	if (!ids || ids.length <= 0) {
		return path.valueOf();
	}
	const pathStr: string = path.valueOf();
	let index = 0;
	return pathStr.replace(/:\w+/g, () => ids[index++]);
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
