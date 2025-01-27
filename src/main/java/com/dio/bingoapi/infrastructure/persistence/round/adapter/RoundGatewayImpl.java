package com.dio.bingoapi.infrastructure.persistence.round.adapter;

import com.dio.bingoapi.domain.round.dto.PageableRoundsDTO;
import com.dio.bingoapi.domain.round.dto.PagedRoundsDTO;
import com.dio.bingoapi.domain.round.entity.Round;
import com.dio.bingoapi.domain.round.gateway.RoundGateway;
import com.dio.bingoapi.infrastructure.persistence.round.mapper.RoundDocumentMapper;
import com.dio.bingoapi.infrastructure.persistence.round.repository.RoundDocumentRepository;
import com.dio.bingoapi.infrastructure.persistence.round.repository.impl.FindOnDemandRoundRepositoryImpl;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoundGatewayImpl implements RoundGateway {

    private final RoundDocumentRepository roundDocumentRepository;
    private final FindOnDemandRoundRepositoryImpl findOnDemandRoundRepository;
    private final RoundDocumentMapper roundDocumentMapper;

    @Override
    public Mono<Round> create(Round round) {
        return roundDocumentRepository.save(roundDocumentMapper.toDocument(round))
                .map(roundDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<PagedRoundsDTO> findOnDemand(PageableRoundsDTO pageable) {
        return findOnDemandRoundRepository.findOnDemand(pageable)
                .collectList()
                .zipWhen(documents -> findOnDemandRoundRepository.count(pageable))
                .map(tuple -> PagedRoundsDTO.builder()
                        .currentPage(pageable.page())
                        .totalPages((tuple.getT2() / pageable.limit())
                                + (((tuple.getT2()) % pageable.limit() > 0) ? 1 : 0))
                        .totalItems(tuple.getT2())
                        .content(tuple.getT1().stream().map(roundDocumentMapper::toDomainModel).toList())
                        .build());
    }

    @Override
    public Flux<Round> findAll() {
        return roundDocumentRepository.findAll()
                .map(roundDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<Round> findById(String id) {
        return roundDocumentRepository.findById(id)
                .map(roundDocumentMapper::toDomainModel);
    }

    @Override
    public Mono<Boolean> existsByIdAndPlayerId(String id, String playerId) {
        return roundDocumentRepository.existsByIdAndBingoCards_playerId(id, playerId);
    }

}
