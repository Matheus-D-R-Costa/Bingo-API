package com.dio.bingoapi.infrastructure.persistence.player.mapper;

import com.dio.bingoapi.domain.player.entity.Player;
import com.dio.bingoapi.infrastructure.persistence.player.document.PlayerDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", imports = java.time.OffsetDateTime.class)
public interface PlayerDocumentMapper {

    Player toDomainModel(PlayerDocument document);

    PlayerDocument toDocument(Player entity);

}
