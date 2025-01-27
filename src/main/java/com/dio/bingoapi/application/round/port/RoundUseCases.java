package com.dio.bingoapi.application.round.port;

import com.dio.bingoapi.application.round.dto.request.PageableRoundsRequestDTO;
import com.dio.bingoapi.application.round.dto.response.BingoCardResponseDTO;
import com.dio.bingoapi.application.round.dto.response.PagedRoundsResponseDTO;
import com.dio.bingoapi.application.round.dto.response.RoundResponseDTO;
import com.dio.bingoapi.application.round.dto.response.SortedNumberResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoundUseCases {

    Mono<RoundResponseDTO> create();
    Mono<SortedNumberResponseDTO> generateNextNumber(String id);
    Mono<BingoCardResponseDTO> generateBingoCard(String id, String playerId);
    Mono<SortedNumberResponseDTO> getLastSortedNumber(String id);
    Mono<RoundResponseDTO> findById(String id);
    Flux<RoundResponseDTO> findAll();
    Mono<PagedRoundsResponseDTO> findOnDemand(PageableRoundsRequestDTO request);

}
