package com.dio.bingoapi.infrastructure.persistence.round.repository;

import com.dio.bingoapi.infrastructure.persistence.round.document.RoundDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoundDocumentRepository extends ReactiveMongoRepository<RoundDocument, String> {

    Mono<Boolean> existsByIdAndBingoCards_playerId(String id, String playerId);

}
