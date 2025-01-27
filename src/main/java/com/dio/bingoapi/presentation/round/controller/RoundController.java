package com.dio.bingoapi.presentation.round.controller;

import com.dio.bingoapi.application.round.dto.request.PageableRoundsRequestDTO;
import com.dio.bingoapi.application.round.dto.response.BingoCardResponseDTO;
import com.dio.bingoapi.application.round.dto.response.PagedRoundsResponseDTO;
import com.dio.bingoapi.application.round.dto.response.RoundResponseDTO;
import com.dio.bingoapi.application.round.dto.response.SortedNumberResponseDTO;
import com.dio.bingoapi.application.round.port.RoundUseCases;
import com.dio.bingoapi.infrastructure.configuration.validation.MongoId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundUseCases roundUseCases;

    @ResponseStatus(CREATED)
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<RoundResponseDTO> create() {
        return roundUseCases.create();
    }

    @PostMapping(value = "{id}/generate-number", produces = APPLICATION_JSON_VALUE)
    public Mono<SortedNumberResponseDTO> generateNextNumber(
            @PathVariable @Valid @MongoId(message = "{roundController.id}") final String id) {
        return roundUseCases.generateNextNumber(id);
    }

    @PostMapping(value = "{id}/bingo-card/{playerId}", produces = APPLICATION_JSON_VALUE)
    public Mono<BingoCardResponseDTO> generateBingoCard(
            @PathVariable @Valid @MongoId(message = "{roundController.id}") final String id,
            @PathVariable @Valid @MongoId(message = "{playerController.id}") final String playerId) {
        return roundUseCases.generateBingoCard(id, playerId);
    }

    @GetMapping(value = "{id}/current-number", produces = APPLICATION_JSON_VALUE)
    public Mono<SortedNumberResponseDTO> getLastSortedNumber(
            @PathVariable @Valid @MongoId(message = "{roundController.id}") final String id) {
        return roundUseCases.getLastSortedNumber(id);
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<RoundResponseDTO> findById(
            @PathVariable @Valid @MongoId(message = "{roundController.id}") final String id) {
        return roundUseCases.findById(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<RoundResponseDTO> findAll() {
        return roundUseCases.findAll();
    }

    @GetMapping(value = "search", produces = APPLICATION_JSON_VALUE)
    public Mono<PagedRoundsResponseDTO> findOnDemand(@Valid final PageableRoundsRequestDTO request) {
        return roundUseCases.findOnDemand(request);
    }

}
