package com.dio.bingoapi.infrastructure.configuration.resources;

import com.dio.bingoapi.application.round.mapper.BingoCardMapper;
import com.dio.bingoapi.application.round.mapper.RoundMapper;
import com.dio.bingoapi.application.round.port.RoundUseCases;
import com.dio.bingoapi.application.round.usecases.RoundUseCasesImpl;
import com.dio.bingoapi.domain.mail.service.MailService;
import com.dio.bingoapi.domain.player.service.PlayerQueryService;
import com.dio.bingoapi.domain.round.gateway.RoundGateway;
import com.dio.bingoapi.domain.round.service.RoundQueryService;
import com.dio.bingoapi.domain.round.service.RoundService;
import com.dio.bingoapi.infrastructure.persistence.round.adapter.RoundGatewayImpl;
import com.dio.bingoapi.infrastructure.persistence.round.mapper.RoundDocumentMapper;
import com.dio.bingoapi.infrastructure.persistence.round.repository.RoundDocumentRepository;
import com.dio.bingoapi.infrastructure.persistence.round.repository.impl.FindOnDemandRoundRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoundConfig {

    @Bean
    public RoundGateway roundGateway(RoundDocumentRepository roundDocumentRepository,
                                     FindOnDemandRoundRepositoryImpl findOnDemandRoundRepository,
                                     RoundDocumentMapper roundDocumentMapper) {

        return new RoundGatewayImpl(roundDocumentRepository, findOnDemandRoundRepository, roundDocumentMapper);

    }

    @Bean
    public RoundQueryService roundQueryService(RoundGateway roundGateway) {
        return new RoundQueryService(roundGateway);
    }

    @Bean
    public RoundService roundService(RoundQueryService roundQueryService, RoundGateway roundGateway,
                                     PlayerQueryService playerQueryService, MailService mailService) {

        return new RoundService(roundQueryService, roundGateway, playerQueryService, mailService);

    }

    @Bean
    public RoundUseCases roundUseCases(RoundService roundService, RoundQueryService roundQueryService,
                                       RoundMapper roundMapper, BingoCardMapper bingoCardMapper) {

        return new RoundUseCasesImpl(roundService, roundQueryService, roundMapper, bingoCardMapper);

    }

}
