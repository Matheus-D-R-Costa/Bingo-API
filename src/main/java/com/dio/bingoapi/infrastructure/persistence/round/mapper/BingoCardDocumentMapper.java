package com.dio.bingoapi.infrastructure.persistence.round.mapper;

import com.dio.bingoapi.domain.round.entity.BingoCard;
import com.dio.bingoapi.infrastructure.persistence.round.document.BingoCardDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BingoCardDocumentMapper {

    @Mapping(target = "id", source = "bingoCardId")
    BingoCard toDomainModel(BingoCardDocument document);

    @Mapping(target = "bingoCardId", source = "id")
    BingoCardDocument toDocument(BingoCard entity);

}
