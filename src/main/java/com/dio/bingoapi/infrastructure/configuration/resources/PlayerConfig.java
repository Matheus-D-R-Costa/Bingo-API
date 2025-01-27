package com.dio.bingoapi.infrastructure.configuration.resources;

import com.dio.bingoapi.application.player.mapper.PlayerMapper;
import com.dio.bingoapi.application.player.port.PlayerUseCases;
import com.dio.bingoapi.application.player.usescase.PlayerUseCaseImpl;
import com.dio.bingoapi.domain.player.gateways.PlayerGateway;
import com.dio.bingoapi.domain.player.service.PlayerQueryService;
import com.dio.bingoapi.domain.player.service.PlayerService;
import com.dio.bingoapi.infrastructure.persistence.player.adapter.PlayerGatewayImpl;
import com.dio.bingoapi.infrastructure.persistence.player.mapper.PlayerDocumentMapper;
import com.dio.bingoapi.infrastructure.persistence.player.repository.PlayerDocumentRepository;
import com.dio.bingoapi.infrastructure.persistence.player.repository.impl.FindOnDemandPlayerRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerConfig {

    @Bean
    public PlayerGateway playerGateway(PlayerDocumentRepository playerDocumentRepository,
                                       FindOnDemandPlayerRepositoryImpl findOnDemandPlayerRepository,
                                       PlayerDocumentMapper playerDocumentMapper) {

        return new PlayerGatewayImpl(playerDocumentRepository, findOnDemandPlayerRepository, playerDocumentMapper);

    }

    @Bean
    PlayerQueryService playerQueryService(PlayerGateway playerGateway) {
        return new PlayerQueryService(playerGateway);
    }

    @Bean
    PlayerService playerService(PlayerQueryService playerQueryService, PlayerGateway playerGateway) {
        return new PlayerService(playerQueryService, playerGateway);
    }

    @Bean
    public PlayerUseCases playerUseCases(PlayerService playerService, PlayerQueryService playerQueryService,
                                         PlayerMapper playerMapper) {

        return new PlayerUseCaseImpl(playerService, playerQueryService, playerMapper);

    }

}
