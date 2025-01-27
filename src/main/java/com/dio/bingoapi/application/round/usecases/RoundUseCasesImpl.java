package com.dio.bingoapi.application.round.usecases;

import com.dio.bingoapi.application.round.dto.request.PageableRoundsRequestDTO;
import com.dio.bingoapi.application.round.dto.response.BingoCardResponseDTO;
import com.dio.bingoapi.application.round.dto.response.PagedRoundsResponseDTO;
import com.dio.bingoapi.application.round.dto.response.RoundResponseDTO;
import com.dio.bingoapi.application.round.dto.response.SortedNumberResponseDTO;
import com.dio.bingoapi.application.round.mapper.BingoCardMapper;
import com.dio.bingoapi.application.round.mapper.RoundMapper;
import com.dio.bingoapi.application.round.port.RoundUseCases;
import com.dio.bingoapi.domain.round.service.RoundQueryService;
import com.dio.bingoapi.domain.round.service.RoundService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoundUseCasesImpl implements RoundUseCases {

    private final RoundService roundService;
    private final RoundQueryService roundQueryService;
    private final RoundMapper roundMapper;
    private final BingoCardMapper bingoCardMapper;

    @Override
    public Mono<RoundResponseDTO> create() {
        return roundService.create()
                .map(roundMapper::toResponse);
    }

    @Override
    public Mono<SortedNumberResponseDTO> generateNextNumber(String id) {
        return roundService.generateNextNumber(id)
                .map(number -> SortedNumberResponseDTO.builder().sortedNumber(number).build());
    }

    @Override
    public Mono<BingoCardResponseDTO> generateBingoCard(String id, String playerId) {
        return roundService.generateBingoCard(id, playerId)
                .map(bingoCardMapper::toResponse);
    }

    @Override
    public Mono<SortedNumberResponseDTO> getLastSortedNumber(String id) {
        return roundQueryService.getLastSortedNumber(id)
                .map(number -> SortedNumberResponseDTO.builder().sortedNumber(number).build());
    }

    @Override
    public Mono<RoundResponseDTO> findById(String id) {
        return roundQueryService.findById(id)
                .map(roundMapper::toResponse);
    }

    @Override
    public Flux<RoundResponseDTO> findAll() {
        return roundQueryService.findAll()
                .map(roundMapper::toResponse);
    }

    @Override
    public Mono<PagedRoundsResponseDTO> findOnDemand(PageableRoundsRequestDTO request) {
        return Mono.just(request)
                .map(roundMapper::toDomainDto)
                .flatMap(roundQueryService::findOnDemand)
                .map(roundMapper::toResponse);
    }
}
