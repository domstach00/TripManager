package com.example.tripmanager.repository.aggregations;

import org.springframework.data.mongodb.core.MongoTemplate;

public class TripPlanAggregationRepositoryImpl implements TripPlanAggregationRepository {
    private final MongoTemplate mongoTemplate;

    public TripPlanAggregationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public double sumTripPlansValue(String tripId) {
        return 1;
    }
}
