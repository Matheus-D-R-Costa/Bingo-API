package com.dio.bingoapi.application.player.mapper;

import com.dio.bingoapi.application.player.dto.request.PageablePlayersRequestDTO;
import com.dio.bingoapi.application.player.dto.request.PlayerRequestDTO;
import com.dio.bingoapi.application.player.dto.response.PagedPlayersResponseDTO;
import com.dio.bingoapi.application.player.dto.response.PlayerResponseDTO;
import com.dio.bingoapi.domain.player.dto.PageablePlayersDTO;
import com.dio.bingoapi.domain.player.dto.PagedPlayersDTO;
import com.dio.bingoapi.domain.player.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Player toDomainModel(PlayerRequestDTO request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Player toDomainModel(PlayerRequestDTO request, String id);

    PlayerResponseDTO toResponse(Player domainModel);

    PagedPlayersResponseDTO toResponse(PagedPlayersDTO domainDto);

    PageablePlayersDTO toDomainDto(PageablePlayersRequestDTO request);

}
