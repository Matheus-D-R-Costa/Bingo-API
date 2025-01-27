package com.dio.bingoapi.infrastructure.persistence.player.adapter;

import com.dio.bingoapi.domain.player.dto.PageablePlayersDTO;
import com.dio.bingoapi.domain.player.dto.PagedPlayersDTO;
import com.dio.bingoapi.domain.player.entity.Player;
import com.dio.bingoapi.domain.player.gateways.PlayerGateway;
import com.dio.bingoapi.infrastructure.persistence.player.mapper.PlayerDocumentMapper;
import com.dio.bingoapi.infrastructure.persistence.player.repository.PlayerDocumentRepository;
import com.dio.bingoapi.infrastructure.persistence.player.repository.impl.FindOnDemandPlayerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PlayerGatewayImpl implements PlayerGateway {

    private final PlayerDocumentRepository playerDocumentRepository;
    private final FindOnDemandPlayerRepositoryImpl findOnDemandPlayerRepository;
    private final PlayerDocumentMapper playerDocumentMapper;

    @Override
    public Mono<Player> create(Player player) {
        return playerDocumentRepository.save(playerDocumentMapper.toDocument(player))
                .map(playerDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<PagedPlayersDTO> findOnDemand(PageablePlayersDTO pageable) {
        return findOnDemandPlayerRepository.findOnDemand(pageable)
                .collectList()
                .zipWhen(documents -> findOnDemandPlayerRepository.count(pageable))
                .map(tuple -> PagedPlayersDTO.builder()
                        .currentPage(pageable.page())
                        .totalPages((tuple.getT2() / pageable.limit())
                                + (((tuple.getT2()) % pageable.limit() > 0) ? 1 : 0))
                        .totalItems(tuple.getT2())
                        .content(tuple.getT1().stream().map(playerDocumentMapper::toDomainModel).toList())
                        .build());
    }

    @Override
    public Mono<Player> findById(String id) {
        return playerDocumentRepository.findById(id)
                .map(playerDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<Player> findByEmail(String email) {
        return playerDocumentRepository.findByEmail(email)
                .map(playerDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<Void> delete(Player player) {
        return playerDocumentRepository.delete(playerDocumentMapper.toDocument(player));
    }
}