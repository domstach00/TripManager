package com.example.tripmanager.trip.repository;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.shared.model.common.Member;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TripRepositoryImpl extends AbstractRepositoryImpl<Trip> implements TripRepository {
    @Override
    public Page<Trip> findAllRelatedTrips(Pageable pageable, Account account) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        buildCriteriaByAccessModifiers(false, false, false, null)
                )
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaIsAccountMemberOrOwnerOfTrip(account)
                )
        );

        return findAllBy(pageable, operationList);
    }

    @Override
    public Optional<Trip> findTripById(String tripId, Account account) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(buildCriteriaById(tripId))
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaByAccessModifiers(false, false, false, null)
                )
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaIsAccountMemberOrOwnerOfTrip(account)
                )
        );

        return findOneBy(operationList);
    }

    @Override
    public Optional<Trip> findTripByIdWhereAccountIsOwnerOrAdmin(String tripId, Account account) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(buildCriteriaById(tripId))
        );
        operationList.add(
                Aggregation.match(
                        buildCriteriaByAccessModifiers(false, false, null, null)
                )
        );
        operationList.add(
                Aggregation.match(
                        new Criteria().orOperator(
                                buildCriteriaAccountIsOwner(account),
                                buildCriteriaAccountIsAdmin(account)
                        )
                )
        );
        return findOneBy(operationList);
    }

    @Override
    protected Class<Trip> getEntityClass() {
        return Trip.class;
    }

    protected Criteria buildCriteriaIsAccountMemberOrOwnerOfTrip(Account account) {
        return new Criteria().orOperator(
                buildCriteriaAccountIsOwner(account),
                buildCriteriaAccountIsMember(account)
        );
    }

    protected Criteria buildCriteriaAccountIsAdmin(Account account) {
        return Criteria.where(Trip.FIELD_NAME_MEMBERS)
                .elemMatch(
                        Criteria.where(Member.FIELD_ACCOUNT_ID).is(new ObjectId(account.getId()))
                                .and(Member.FIELD_MEMBER_ROLE).is(Member.MemberRole.ADMINISTRATOR.name()));
    }

    protected Criteria buildCriteriaAccountIsOwner(Account account) {
        return Criteria.where(Trip.FIELD_NAME_OWNER)
                .is(new ObjectId(account.getId()));
    }
    protected Criteria buildCriteriaAccountIsMember(Account account) {
        return Criteria.where(Trip.FIELD_NAME_MEMBERS + "." + Member.FIELD_ACCOUNT_ID)
                .is(new ObjectId(account.getId()));
    }

    protected Criteria buildCriteriaByAccessModifiers(
            @Nullable Boolean isDeleted,
            @Nullable Boolean isArchived,
            @Nullable Boolean isClosed,
            @Nullable Boolean isPublic) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (!Objects.isNull(isDeleted)) {
            criteriaList.add(buildCriteriaByIsDeleted(isDeleted));
        }
        if (!Objects.isNull(isArchived)) {
            criteriaList.add(buildCriteriaByIsArchived(isArchived));
        }
        if (!Objects.isNull(isClosed)) {
            criteriaList.add(buildCriteriaByIsClosed(isClosed));
        }
        if (!Objects.isNull(isPublic)) {
            criteriaList.add(buildCriteriaByIsPublic(isPublic));
        }
        return criteriaList.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    protected Criteria buildCriteriaByIsDeleted(boolean isDeleted) {
        return buildCriteriaByFlag(Trip.FIELD_NAME_IS_DELETED, isDeleted);
    }

    protected Criteria buildCriteriaByIsArchived(boolean isArchived) {
        return buildCriteriaByFlag(Trip.FIELD_NAME_IS_ARCHIVED, isArchived);
    }

    protected Criteria buildCriteriaByIsClosed(boolean isClosed) {
        return buildCriteriaByFlag(Trip.FIELD_NAME_IS_CLOSED, isClosed);
    }

    protected Criteria buildCriteriaByIsPublic(boolean isPublic) {
        return buildCriteriaByFlag(Trip.FIELD_NAME_IS_CLOSED, isPublic);
    }
}
