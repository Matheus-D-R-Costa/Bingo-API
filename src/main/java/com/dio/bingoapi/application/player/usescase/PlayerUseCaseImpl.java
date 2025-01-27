package com.dio.bingoapi.application.player.usescase;

import com.dio.bingoapi.application.player.dto.request.PageablePlayersRequestDTO;
import com.dio.bingoapi.application.player.dto.request.PlayerRequestDTO;
import com.dio.bingoapi.application.player.dto.response.PagedPlayersResponseDTO;
import com.dio.bingoapi.application.player.dto.response.PlayerResponseDTO;
import com.dio.bingoapi.application.player.mapper.PlayerMapper;
import com.dio.bingoapi.application.player.port.PlayerUseCases;
import com.dio.bingoapi.domain.player.service.PlayerQueryService;
import com.dio.bingoapi.domain.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PlayerUseCaseImpl implements PlayerUseCases {

    private final PlayerService playerService;
    private final PlayerQueryService playerQueryService;
    private final PlayerMapper playerMapper;

    @Override
    public Mono<PlayerResponseDTO> create(PlayerRequestDTO request) {
        return Mono.just(request)
                .map(playerMapper::toDomainModel)
                .flatMap(playerService::create)
                .map(playerMapper::toResponse);
    }

    @Override
    public Mono<PlayerResponseDTO> update(String id, PlayerRequestDTO request) {
        return Mono.just(request)
                .map(requestBody -> playerMapper.toDomainModel(request, id))
                .flatMap(playerService::update)
                .map(playerMapper::toResponse);
    }

    @Override
    public Mono<PlayerResponseDTO> findById(String id) {
        return playerQueryService.findById(id)
                .map(playerMapper::toResponse);
    }

    @Override
    public Mono<PagedPlayersResponseDTO> findOnDemand(PageablePlayersRequestDTO request) {
        return Mono.just(request)
                .map(playerMapper::toDomainDto)
                .flatMap(playerQueryService::findOnDemand)
                .map(playerMapper::toResponse);
    }

    @Override
    public Mono<Void> delete(String id) {
        return playerService.delete(id);
    }
}
