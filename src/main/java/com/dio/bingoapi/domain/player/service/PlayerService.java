package com.dio.bingoapi.domain.player.service;

import com.dio.bingoapi.domain.player.entity.Player;
import com.dio.bingoapi.domain.player.exception.EmailAlreadyUsedException;
import com.dio.bingoapi.domain.player.gateways.PlayerGateway;
import com.dio.bingoapi.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.PLAYER_EMAIL_ALREADY_USED;

@RequiredArgsConstructor
public class PlayerService {

    private final PlayerQueryService playerQueryService;
    private final PlayerGateway playerGateway;

    public Mono<Player> create(Player player) {
        return playerQueryService.findByEmail(player.email())
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyUsedException(
                        PLAYER_EMAIL_ALREADY_USED.params(player.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> playerGateway.create(player));
    }

    public Mono<Player> update(Player player) {
        return verifyEmail(player)
                .then(Mono.defer(() -> playerQueryService.findById(player.id())))
                .map(playerInDb -> player.toBuilder()
                        .createdAt(playerInDb.createdAt())
                        .updatedAt(playerInDb.updatedAt())
                        .build())
                .flatMap(playerGateway::create);
    }

    private Mono<Void> verifyEmail(Player player) {
        return playerQueryService.findByEmail(player.email())
                .filter(playerInDb -> player.id().equals(playerInDb.id()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyUsedException(
                        PLAYER_EMAIL_ALREADY_USED.params(player.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }

    public Mono<Void> delete(String id) {
        return playerQueryService.findById(id)
                .flatMap(playerGateway::delete);
    }

}
