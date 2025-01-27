package com.dio.bingoapi.domain.player.service;

import com.dio.bingoapi.domain.player.dto.PageablePlayersDTO;
import com.dio.bingoapi.domain.player.dto.PagedPlayersDTO;
import com.dio.bingoapi.domain.player.entity.Player;
import com.dio.bingoapi.domain.player.gateways.PlayerGateway;
import com.dio.bingoapi.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.PLAYER_NOT_FOUND_WITH_EMAIL;
import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.PLAYER_NOT_FOUND_WITH_ID;

@RequiredArgsConstructor
public class PlayerQueryService {

    private final PlayerGateway playerGateway;


    public Mono<Player> findByEmail(String email) {
        return playerGateway.findByEmail(email)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(
                        PLAYER_NOT_FOUND_WITH_EMAIL.params(email).getMessage()))));
    }

    public Mono<Player> findById(String id) {
        return playerGateway.findById(id)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(
                        PLAYER_NOT_FOUND_WITH_ID.params(id).getMessage()))));
    }

    public Mono<PagedPlayersDTO> findOnDemand(PageablePlayersDTO pageable) {
        return playerGateway.findOnDemand(pageable);
    }

}
