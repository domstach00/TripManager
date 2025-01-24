package com.example.tripmanager.repository;

import com.example.tripmanager.model.common.Member;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripPlanRepositoryImpl extends AbstractRepositoryImpl<TripPlan> implements TripPlanRepository {
    @Override
    protected Class<TripPlan> getEntityClass() {
        return TripPlan.class;
    }


    @Override
    public Page<TripPlan> findAllByTripId(Pageable pageable, String tripId, String accountId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaIsPlanRelatedToTripId(tripId)
                )
        );

        final String tripLookedUp = "_" + TripPlan.FIELD_NAME_TRIP_ID;
        operationList.add(
                Aggregation.lookup(Trip.COLLECTION_NAME, TripPlan.FIELD_NAME_TRIP_ID, FIELD_NAME_ID_WITH_UNDERSCORE, tripLookedUp)
        );

        operationList.add(
                buildUnwindAggregationOperation(tripLookedUp)
        );

        operationList.add(
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_OWNER).is(new ObjectId(accountId)),
                                Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_MEMBERS + "." + Member.FIELD_ACCOUNT_ID).is(new ObjectId(accountId))
                        )
                )
        );

        return findAllBy(pageable, operationList);
    }

    @Override
    public Optional<TripPlan> findByIdWhereUserIsAdmin(String tripPlanId, String accountId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaById(tripPlanId)
                )
        );

        final String tripLookedUp = "_" + TripPlan.FIELD_NAME_TRIP_ID;
        operationList.add(
                Aggregation.lookup(Trip.COLLECTION_NAME, TripPlan.FIELD_NAME_TRIP_ID, FIELD_NAME_ID_WITH_UNDERSCORE, tripLookedUp)
        );

        operationList.add(
                buildUnwindAggregationOperation(tripLookedUp)
        );

        operationList.add(
                Aggregation.match(new Criteria().orOperator(
                        Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_OWNER).is(new ObjectId(accountId)),
                        Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_MEMBERS).elemMatch(
                                Criteria.where(Member.FIELD_ACCOUNT_ID).is(new ObjectId(accountId))
                                        .and(Member.FIELD_MEMBER_ROLE).is(Member.MemberRole.ADMINISTRATOR.name())
                        )
                ))
        );

        return findOneBy(operationList);
    }

    @Override
    public Page<TripPlan> findAllTripPlansWithMapElementForGivenTripId(Pageable pageable, String tripId, String accountId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaIsPlanRelatedToTripId(tripId)
                )
        );

        operationList.add(
                Aggregation.match(
                        buildCriteriaDoesMapElementValueExists(true)
                )
        );


        final String tripLookedUp = "_" + TripPlan.FIELD_NAME_TRIP_ID;
        operationList.add(
                Aggregation.lookup(Trip.COLLECTION_NAME, TripPlan.FIELD_NAME_TRIP_ID, FIELD_NAME_ID_WITH_UNDERSCORE, tripLookedUp)
        );

        operationList.add(
                buildUnwindAggregationOperation(tripLookedUp)
        );

        operationList.add(
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_OWNER).is(new ObjectId(accountId)),
                                Criteria.where(tripLookedUp + "." + Trip.FIELD_NAME_MEMBERS + "." + Member.FIELD_ACCOUNT_ID).is(new ObjectId(accountId))
                        )
                )
        );

        return findAllBy(pageable, operationList);
    }

    protected Criteria buildCriteriaIsPlanRelatedToTripId(String tripId) {
        return Criteria.where(TripPlan.FIELD_NAME_TRIP_ID).is(new ObjectId(tripId));
    }

    protected Criteria buildCriteriaDoesMapElementValueExists(boolean exists) {
        if (exists) {
            return Criteria.where(TripPlan.FIELD_NAME_MAP_ELEMENT).ne(null);
        } else {
            return Criteria.where(TripPlan.FIELD_NAME_MAP_ELEMENT).is(null);
        }
    }
}
