package com.dio.bingoapi.domain.player.gateways;

import com.dio.bingoapi.domain.player.dto.PageablePlayersDTO;
import com.dio.bingoapi.domain.player.dto.PagedPlayersDTO;
import com.dio.bingoapi.domain.player.entity.Player;
import reactor.core.publisher.Mono;

public interface PlayerGateway {

    Mono<Player> create(Player player);
    Mono<PagedPlayersDTO> findOnDemand(PageablePlayersDTO pageable);
    Mono<Player> findById(String id);
    Mono<Player> findByEmail(String email);
    Mono<Void> delete(Player player);

}
