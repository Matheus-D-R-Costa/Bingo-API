package com.dio.bingoapi.domain.round.gateway;

import com.dio.bingoapi.domain.round.dto.PageableRoundsDTO;
import com.dio.bingoapi.domain.round.dto.PagedRoundsDTO;
import com.dio.bingoapi.domain.round.entity.Round;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoundGateway {

    Mono<Round> create(Round round);
    Mono<PagedRoundsDTO> findOnDemand(PageableRoundsDTO pageable);
    Flux<Round> findAll();
    Mono<Round> findById(String id);
    Mono<Boolean> existsByIdAndPlayerId(String id, String playerId);

}
