package com.dio.bingoapi.domain.round.service;

import com.dio.bingoapi.domain.mail.dto.MailMessageDTO;
import com.dio.bingoapi.domain.mail.service.MailService;
import com.dio.bingoapi.domain.player.service.PlayerQueryService;
import com.dio.bingoapi.domain.round.entity.BingoCard;
import com.dio.bingoapi.domain.round.entity.Round;
import com.dio.bingoapi.domain.round.gateway.RoundGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class RoundService {

    private final RoundQueryService roundQueryService;
    private final RoundGateway roundGateway;
    private final PlayerQueryService playerQueryService;
    private final MailService mailService;

    public Mono<Round> create() {
        return Round.builder().create()
                .map(Round.RoundBuilder::build)
                .flatMap(roundGateway::create);
    }

    public Mono<Integer> generateNextNumber(String id) {
        return roundQueryService.findById(id)
                .flatMap(round -> round.toBuilder().sortNumber())
                .map(Round.RoundBuilder::build)
                .flatMap(roundGateway::create)
                .flatMap(round -> round.winnersIds().isEmpty()
                        ? round.getLastSortedNumber()
                        : round.getLastSortedNumber()
                        .onTerminateDetach()
                        .doOnSuccess(lastSortedNumber -> processIfHasWinners(round)));
    }

    public Mono<BingoCard> generateBingoCard(String id, String playerId) {
        return roundQueryService.verifyIfExistsByIdAndPlayerId(id, playerId)
                .then(Mono.defer(() -> playerQueryService.findById(playerId)))
                .zipWhen(player -> roundQueryService.findById(id))
                .flatMap(tuple -> tuple.getT2().toBuilder().addBingoCard(tuple.getT1()))
                .map(Round.RoundBuilder::build)
                .flatMap(roundGateway::create)
                .map(round -> round.bingoCards().stream()
                        .filter(bingoCard -> bingoCard.playerId().equals(playerId))
                        .toList()
                        .get(0));
    }

    private void processIfHasWinners(Round round) {
        Flux.fromIterable(round.bingoCards())
                .flatMap(bingoCard -> round.winnersIds().contains(bingoCard.playerId())
                        ? notifyWinner(round, bingoCard)
                        : notifyPlayer(round, bingoCard))
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    private Mono<Void> notifyPlayer(Round round, BingoCard bingoCard) {
        return playerQueryService.findById(bingoCard.playerId())
                .map(player -> MailMessageDTO.create(round, player, bingoCard))
                .flatMap(mailService::send);
    }

    private Mono<Void> notifyWinner(Round round, BingoCard bingoCard) {
        return playerQueryService.findById(bingoCard.playerId())
                .map(player -> MailMessageDTO.createWinner(round, player, bingoCard))
                .flatMap(mailService::send);
    }

}
