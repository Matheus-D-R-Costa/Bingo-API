package com.dio.bingoapi.infrastructure.persistence.round.repository.impl;

import com.dio.bingoapi.domain.round.dto.PageableRoundsDTO;
import com.dio.bingoapi.infrastructure.persistence.round.document.RoundDocument;
import com.dio.bingoapi.shared.utils.QueryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.dio.bingoapi.shared.enums.SortDirection.ASC;

@Repository
@RequiredArgsConstructor
public class FindOnDemandRoundRepositoryImpl {

    private final ReactiveMongoTemplate template;
    private final QueryBuilder queryBuilder;

    public Flux<RoundDocument> findOnDemand(final PageableRoundsDTO pageable) {
        return queryBuilder.buildWhere(new Query(), pageable.sentence(), "state")
                .flatMap(query -> queryBuilder
                        .buildDateCriteria(query, "createdAt", pageable.startDate(), pageable.endDate()))
                .map(query -> query
                        .with(pageable.sortDirection() == ASC
                                ? Sort.by(pageable.sortBy().getField()).ascending()
                                : Sort.by(pageable.sortBy().getField()).descending())
                        .skip(pageable.getSkip())
                        .limit(pageable.limit()))
                .flatMapMany(query -> template.find(query, RoundDocument.class));
    }

    public Mono<Long> count(final PageableRoundsDTO pageable) {
        return queryBuilder.buildWhere(new Query(), pageable.sentence(), "state")
                .flatMap(query -> queryBuilder
                        .buildDateCriteria(query, "createdAt", pageable.startDate(), pageable.endDate()))
                .flatMap(query -> template.count(query, RoundDocument.class));
    }

}
