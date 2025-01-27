package com.dio.bingoapi.infrastructure.persistence.player.repository;

import com.dio.bingoapi.infrastructure.persistence.player.document.PlayerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerDocumentRepository extends ReactiveMongoRepository<PlayerDocument, String> {

    Mono<PlayerDocument> findByEmail(String email);

}
