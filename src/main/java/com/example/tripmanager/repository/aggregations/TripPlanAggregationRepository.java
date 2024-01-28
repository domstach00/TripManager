package com.example.tripmanager.repository.aggregations;

public interface TripPlanAggregationRepository {
    double sumTripPlansValue(String tripId);
}
