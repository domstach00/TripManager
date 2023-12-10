export enum ApiPath {
  apiBaseUrl = 'http://localhost:8080/api/',
  login = 'auth/login',
  register = 'auth/register',
  trip = 'trip',
  tripSelect = 'trip/{0}',
  tripPlan = 'trip/{0}/plan',
  tripPlanSelect = 'trip/plan/{0}',
  googleMapPin = 'googleMapPin',
}
