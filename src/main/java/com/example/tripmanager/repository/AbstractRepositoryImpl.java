package com.example.tripmanager.repository;

import com.example.tripmanager.model.AbstractEntity;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Repository
public abstract class AbstractRepositoryImpl<T extends AbstractEntity> implements MongoRepository<T, String> {
    public static final String DELIMITER_VALUE_LIST = " *[,;:] *";
    public static final String FIELD_NAME_ID_WITH_UNDERSCORE = "_id";
    public static final String FIELD_NAME_CLASS = "_class";
    public static final String FULL_WORD_MATCH_FLAG = "fullWordMatch::";
    public static final String PARTIAL_WORD_MACH_FLAG = "partialWordMatch::";

    @Autowired
    private MongoMappingContext mongoMappingContext;
    @Autowired
    private MongoOperations mongoOperations;

    protected abstract Class<T> getEntityClass();

    protected MongoOperations getMongoOperations() {
        return mongoOperations;
    }

    protected Criteria buildCriteriaById(String id) {
        return Criteria.where(FIELD_NAME_ID_WITH_UNDERSCORE).is(id);
    }

    protected Criteria buildCriteriaByIds(Iterable<String> ids) {
        return Criteria.where(FIELD_NAME_ID_WITH_UNDERSCORE).in(ids);
    }

    protected Criteria buildCriteriaByFlag(String fieldName, boolean flag) {
        return flag
                ? Criteria.where(fieldName).is(true)
                : Criteria.where(fieldName).ne(true);
    }

    protected Page<T> findAllBy(final Pageable pageable, final List<AggregationOperation> operationList) {
        return findAllBy(pageable, operationList, getEntityClass(), getEntityClass());
    }

    protected <I, O> Page<O> findAllBy(final Pageable pageable, final List<AggregationOperation> operationList, Class<I> inputClass, Class<O> outputClass) {
        final int total = countForGivenAggregation(operationList);
        if (total <= 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, total);
        }

        operationList.addAll(buildPageableOperationList(pageable));
        Aggregation searchAggregation = Aggregation.newAggregation(operationList);
        List<O> result = getMongoOperations()
                .aggregate(searchAggregation, inputClass, outputClass)
                .getMappedResults();
        return new PageImpl<>(result, pageable, total);
    }

    protected Optional<T> findOneBy(final List<AggregationOperation> operationList) {
        return findOneBy(operationList, getEntityClass(), getEntityClass());
    }

    protected <I, O> Optional<O> findOneBy(final List<AggregationOperation> operationList, Class<I> inputClass, Class<O> outputClass) {
        operationList.add(
                Aggregation.limit(1)
        );
        Aggregation searchAggregation = Aggregation.newAggregation(operationList);
        AggregationResults<O> results = getMongoOperations().aggregate(searchAggregation, inputClass, outputClass);
        return Optional.ofNullable(results.getUniqueMappedResult());
    }

    protected int countForGivenAggregation(final List<AggregationOperation> operationList) {
        final String FIELD_COUNT = "_count";
        List<AggregationOperation> countOperationList = new ArrayList<>(operationList);
        countOperationList.add(Aggregation.count().as(FIELD_COUNT));

        Aggregation countAggregation = Aggregation.newAggregation(countOperationList);
        AggregationResults<Document> countResult = getMongoOperations().aggregate(countAggregation, getEntityClass(), Document.class);
        return Objects.requireNonNull(countResult.getUniqueMappedResult()).getInteger(FIELD_COUNT);
    }

    protected List<AggregationOperation> buildPageableOperationList(Pageable pageable) {
        List<AggregationOperation> operationList = new ArrayList<>();
        if (pageable == null) {
            return operationList;
        }
        if (pageable.getOffset() > 0) {
            operationList.add(Aggregation.skip(pageable.getOffset()));
        }
        if (pageable.getPageSize() > 0) {
            operationList.add(Aggregation.limit(pageable.getPageSize()));
        }

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            operationList.add(Aggregation.sort(sort));
        }

        return operationList;
    }

    @Override
    @NonNull
    public <S extends T> S insert(@NonNull S entity) {
        return mongoOperations.insert(entity);
    }

    @Override
    @NonNull
    public <S extends T> List<S> insert(@NonNull Iterable<S> entities) {
        List<S> entityList = new ArrayList<>();
        entities.forEach(entityList::add);
        return mongoOperations.insertAll(entityList).stream().toList();
    }

    @Override
    @NonNull
    public <S extends T> Optional<S> findOne(@NonNull Example<S> example) {
        Query query = new Query(Criteria.byExample(example));
        return Optional.ofNullable(mongoOperations.findOne(query, example.getProbeType()));
    }

    @Override
    @NonNull
    public <S extends T> List<S> findAll(@NonNull Example<S> example) {
        Query query = new Query(Criteria.byExample(example));
        return mongoOperations.find(query, example.getProbeType());
    }

    public List<T> findAllByQuery(Query query) {
        return mongoOperations.find(query, getEntityClass());
    }

    @Override
    @NonNull
    public <S extends T> List<S> findAll(@NonNull Example<S> example, @NonNull Sort sort) {
        Query query = new Query(Criteria.byExample(example)).with(sort);
        return mongoOperations.find(query, example.getProbeType());
    }

    @Override
    @NonNull
    public <S extends T> Page<S> findAll(@NonNull Example<S> example, @NonNull Pageable pageable) {
        Query query = new Query(Criteria.byExample(example)).with(pageable);
        List<S> list = mongoOperations.find(query, example.getProbeType());
        return new PageImpl<>(list, pageable, mongoOperations.count(query, example.getProbeType()));
    }

    @Override
    @NonNull
    public <S extends T> long count(@NonNull Example<S> example) {
        Query query = new Query(Criteria.byExample(example));
        return mongoOperations.count(query, example.getProbeType());
    }

    @Override
    @NonNull
    public <S extends T> boolean exists(@NonNull Example<S> example) {
        Query query = new Query(Criteria.byExample(example));
        return mongoOperations.exists(query, example.getProbeType());
    }


    @Override
    @NonNull
    public <S extends T, R> R findBy(@NonNull Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return queryFunction.apply(new FluentQuery.FetchableFluentQuery<S>() {
            
            private Query createQueryFromExample(Example<S> exampleToCreateQuery) {
                S probe = exampleToCreateQuery.getProbe();
                Query query = new Query();

                Arrays.stream(probe.getClass().getDeclaredFields()).forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(probe);
                        if (value != null) {
                            query.addCriteria(new Criteria().and(field.getName()).is(value));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                return query;
            }

            @Override
            @NonNull
            public FetchableFluentQuery<S> sortBy(@NonNull Sort sort) {
                Query query = createQueryFromExample(example).with(sort);
                return this;
            }

            @Override
            @NonNull
            public <R> FetchableFluentQuery<R> as(@NonNull Class<R> resultType) {
                throw new UnsupportedOperationException("Operation 'as' is currently not supported");
            }

            @Override
            @NonNull
            public FetchableFluentQuery<S> project(@NonNull Collection<String> properties) {
                Query query = createQueryFromExample(example);
                properties.forEach(property -> query.fields().include(property));
                return this;
            }

            @Override
            public S oneValue() {
                Query query = createQueryFromExample(example);
                return mongoOperations.findOne(query, example.getProbeType());
            }

            @Override
            public S firstValue() {
                Query query = createQueryFromExample(example).limit(1);
                return mongoOperations.find(query, example.getProbeType()).stream()
                        .findFirst()
                        .orElse(null);
            }

            @Override
            @NonNull
            public List<S> all() {
                Query query = createQueryFromExample(example);
                return mongoOperations.find(query, example.getProbeType());
            }

            @Override
            @NonNull
            public Page<S> page(@NonNull Pageable pageable) {
                Query query = createQueryFromExample(example);
                final long total = mongoOperations.count(query, example.getProbeType());
                List<S> content = mongoOperations.find(query.with(pageable), example.getProbeType());
                return new PageImpl<>(content, pageable, total);
            }

            @Override
            @NonNull
            public Stream<S> stream() {
                Query query = createQueryFromExample(example);
                return mongoOperations.stream(query, example.getProbeType());
            }

            @Override
            public long count() {
                Query query = createQueryFromExample(example);
                return mongoOperations.count(query, example.getProbeType());
            }

            @Override
            public boolean exists() {
                Query query = createQueryFromExample(example);
                return mongoOperations.exists(query, example.getProbeType());
            }
        });
    }

    @Override
    @NonNull
    public <S extends T> S save(@NonNull S entity) {
        return mongoOperations.save(entity);
    }

    @Override
    @NonNull
    public <S extends T> List<S> saveAll(@NonNull Iterable<S> entities) {
        List<S> entityList = new ArrayList<>();
        entities.forEach(entityList::add);

        mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass())
                .insert(entityList)
                .execute();
        return entityList;
    }

    @Override
    @NonNull
    public Optional<T> findById(@NonNull String id) {
        return Optional.ofNullable(
                mongoOperations.findById(id, getEntityClass())
        );
    }

    @Override
    @NonNull
    public boolean existsById(@NonNull String id) {
        Query query = new Query(buildCriteriaById(id));
        return this.mongoOperations.exists(query, getEntityClass());
    }

    @Override
    @NonNull
    public List<T> findAll() {
        return mongoOperations.findAll(getEntityClass());
    }

    @Override
    @NonNull
    public List<T> findAllById(@NonNull Iterable<String> ids) {
        Query query = new Query(buildCriteriaByIds(ids));
        return mongoOperations.find(query, getEntityClass());
    }

    @Override
    public long count() {
        return mongoOperations.count(new Query(), getEntityClass());
    }

    @Override
    public void deleteById(@NonNull String id) {
        mongoOperations.remove(new Query(buildCriteriaById(id)), getEntityClass());
    }

    @Override
    public void delete(@NonNull T entity) {
        mongoOperations.remove(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteAllById(@NonNull Iterable<? extends String> ids) {
        Query query = new Query(buildCriteriaByIds((Iterable<String>) ids));
        mongoOperations.remove(query, getEntityClass());
    }

    @Override
    public void deleteAll(@NonNull Iterable<? extends T> entities) {
        List<String> idsToDelete = new ArrayList<>();
        entities.forEach(entity -> idsToDelete.add(entity.getId()));

        Query query = new Query(buildCriteriaByIds(idsToDelete));
        mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass())
                .remove(query)
                .execute();
    }

    @Override
    public void deleteAll() {
        mongoOperations.remove(new Query(), getEntityClass());
    }

    @Override
    @NonNull
    public List<T> findAll(@NonNull Sort sort) {
        Query query = new Query().with(sort);
        return mongoOperations.find(query, getEntityClass());
    }

    @Override
    @NonNull
    public Page<T> findAll(@NonNull Pageable pageable) {
        Query query = new Query().with(pageable);
        List<T> list = mongoOperations.find(query, getEntityClass());
        return new PageImpl<>(list, pageable, mongoOperations.count(query, getEntityClass()));
    }
}
