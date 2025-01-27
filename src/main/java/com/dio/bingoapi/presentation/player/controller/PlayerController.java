package com.dio.bingoapi.presentation.player.controller;

import com.dio.bingoapi.application.player.dto.request.PageablePlayersRequestDTO;
import com.dio.bingoapi.application.player.dto.request.PlayerRequestDTO;
import com.dio.bingoapi.application.player.dto.response.PagedPlayersResponseDTO;
import com.dio.bingoapi.application.player.dto.response.PlayerResponseDTO;
import com.dio.bingoapi.application.player.port.PlayerUseCases;
import com.dio.bingoapi.infrastructure.configuration.validation.MongoId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerUseCases playerUseCases;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<PlayerResponseDTO> create(@RequestBody @Valid PlayerRequestDTO request) {
        return playerUseCases.create(request);
    }

    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Mono<PlayerResponseDTO> update(@PathVariable @Valid @MongoId(message = "{playercontroller.id}") String id,
                                          @RequestBody @Valid PlayerRequestDTO request) {

        return playerUseCases.update(id, request);

    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<PlayerResponseDTO> findById(@PathVariable @Valid @MongoId(message = "{playercontroller.id}") String id) {
        return playerUseCases.findById(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<PagedPlayersResponseDTO> findOnDemand(@Valid PageablePlayersRequestDTO request) {
        return playerUseCases.findOnDemand(request);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{playercontroller.id}") String id) {
        return playerUseCases.delete(id);
    }

}
