package com.dio.bingoapi.application.player.port;

import com.dio.bingoapi.application.player.dto.request.PageablePlayersRequestDTO;
import com.dio.bingoapi.application.player.dto.request.PlayerRequestDTO;
import com.dio.bingoapi.application.player.dto.response.PagedPlayersResponseDTO;
import com.dio.bingoapi.application.player.dto.response.PlayerResponseDTO;
import reactor.core.publisher.Mono;

public interface PlayerUseCases {

    Mono<PlayerResponseDTO> create(PlayerRequestDTO request);
    Mono<PlayerResponseDTO> update(String id, PlayerRequestDTO request);
    Mono<PlayerResponseDTO> findById(String id);
    Mono<PagedPlayersResponseDTO> findOnDemand(PageablePlayersRequestDTO  request);
    Mono<Void> delete(String id);

}
