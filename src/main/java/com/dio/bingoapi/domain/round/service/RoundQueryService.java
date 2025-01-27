package com.dio.bingoapi.domain.round.service;

import com.dio.bingoapi.domain.round.dto.PageableRoundsDTO;
import com.dio.bingoapi.domain.round.dto.PagedRoundsDTO;
import com.dio.bingoapi.domain.round.entity.Round;
import com.dio.bingoapi.domain.round.exception.BingoCardAlreadyExistsException;
import com.dio.bingoapi.domain.round.exception.RoundNotStartedException;
import com.dio.bingoapi.domain.round.gateway.RoundGateway;
import com.dio.bingoapi.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.*;

@RequiredArgsConstructor
public class RoundQueryService {

    private final RoundGateway roundGateway;

    public Mono<Round> findById(String id) {
        return roundGateway.findById(id)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(
                        ROUND_NOT_FOUND.params(id).getMessage()))));
    }

    public Flux<Round> findAll() {
        return roundGateway.findAll();
    }

    public Mono<PagedRoundsDTO> findOnDemand(PageableRoundsDTO pageable) {
        return roundGateway.findOnDemand(pageable);
    }

    public Mono<Void> verifyIfExistsByIdAndPlayerId(String id, String playerId) {
        return roundGateway.existsByIdAndPlayerId(id, playerId)
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BingoCardAlreadyExistsException(
                        BINGO_CARD_ALREADY_EXISTS.params(playerId, id).getMessage()))))
                .then();
    }

    public Mono<Integer> getLastSortedNumber(String id) {
        return this.findById(id)
                .flatMap(Round::getLastSortedNumber)
                .onErrorResume(NoSuchElementException.class, e -> Mono
                        .error(new RoundNotStartedException(ROUND_NOT_STARTED.params(id).getMessage())));
    }

}
