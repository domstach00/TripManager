export enum ApiPath {
	apiBaseUrl = 'http://localhost:8080/api',
	login = '/auth/login',
	logout = '/auth/logout',
	register = '/auth/register',
	currentAccount = '/auth/currentaccount',
	trip = '/trip',
	tripSelect = trip + '/{0}',
	tripArchive =  tripSelect + '/archive',
	tripDuplicate = tripSelect + '/duplicate',
	tripPlan = tripSelect + '/plan',
	tripPlanSelect = tripSelect + '/plan/{0}',
	googleMapPin = tripSelect + '/googleMapPin',
}
