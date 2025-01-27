package com.dio.bingoapi.shared.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Component
public class QueryBuilder {

    public Mono<Query> buildWhere(Query query, String sentence, List<String> fields) {
        return Mono.just(query)
                .filter(q -> !sentence.isEmpty())
                .switchIfEmpty(Mono.defer(() -> Mono.just(query)))
                .flatMapMany(q -> Flux.fromIterable(fields))
                .map(field -> Criteria.where(field).regex(sentence, "i"))
                .collectList()
                .flatMap(criteriaList -> setWhereClause(query, criteriaList));
    }

    private Mono<Query> setWhereClause(Query query, List<Criteria> criteriaList) {
        return Mono.fromCallable(() -> {
            Criteria whereClause = new Criteria().orOperator(criteriaList);
            return query.addCriteria(whereClause);
        });
    }

    public Mono<Query> buildWhere(Query query, String sentence, String field) {
        return Mono.just(query)
                .filter(q -> !sentence.isEmpty())
                .switchIfEmpty(Mono.defer(() -> Mono.just(query)))
                .flatMap(q -> setWhereClause(q, Criteria.where(field).regex(sentence, "i")));

    }

    private Mono<Query> setWhereClause(Query query, Criteria criteria) {
        return Mono.fromCallable(() -> query.addCriteria(criteria));
    }

    public Mono<Query> buildDateCriteria(Query query, String dateFieldName, OffsetDateTime startDate,
                                         OffsetDateTime endDate) {

        return Mono.fromCallable(() -> {
            Date start = toDate(startDate);
            Date end = toDate(endDate);
            Criteria dateCriteria = Criteria.where(dateFieldName).gte(start).lte(end);
            return query.addCriteria(dateCriteria);
        });

    }

    private Date toDate(OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }

}
