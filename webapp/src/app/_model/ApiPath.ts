export enum ApiPath {
	apiBaseUrl = 'http://localhost:8080/api',
	login = '/auth/login',
	logout = '/auth/logout',
	register = '/auth/register',
	currentAccount = '/auth/currentaccount',
	trip = '/trip',
	tripSelect = '/trip/{0}',
	tripPlan = '/trip/{0}/plan',
	tripPlanSelect = '/trip/{0}/plan/{0}',
	googleMapPin = '/trip/{0}/googleMapPin',
}
