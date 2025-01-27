package com.dio.bingoapi.infrastructure.persistence.player.repository.impl;

import com.dio.bingoapi.domain.player.dto.PageablePlayersDTO;
import com.dio.bingoapi.infrastructure.persistence.player.document.PlayerDocument;
import com.dio.bingoapi.shared.utils.QueryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.dio.bingoapi.shared.enums.SortDirection.ASC;

@Repository
@RequiredArgsConstructor
public class FindOnDemandPlayerRepositoryImpl {

    private final ReactiveMongoTemplate template;
    private final QueryBuilder queryBuilder;

    public Flux<PlayerDocument> findOnDemand(PageablePlayersDTO pageable) {
        return queryBuilder.buildWhere(new Query(), pageable.sentence(), List.of("name", "email"))
                .map(query -> query
                        .with(pageable.sortDirection() == ASC
                                ? Sort.by(pageable.sortBy().getField()).ascending()
                                : Sort.by(pageable.sortBy().getField()).descending())
                        .skip(pageable.getSkip())
                        .limit(pageable.limit()))
                .flatMapMany(query -> template.find(query, PlayerDocument.class));
    }

    public Mono<Long> count(PageablePlayersDTO pageable) {
        return queryBuilder.buildWhere(new Query(), pageable.sentence(), List.of("name", "email"))
                .flatMap(query -> template.count(query, PlayerDocument.class));
    }

}
