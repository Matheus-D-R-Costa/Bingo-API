package com.dio.bingoapi.infrastructure.persistence.round.mapper;

import com.dio.bingoapi.domain.round.entity.Round;
import com.dio.bingoapi.infrastructure.persistence.round.document.RoundDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BingoCardDocumentMapper.class})
public interface RoundDocumentMapper {

    @Mapping(target = "bingoCard", ignore = true)
    @Mapping(target = "sortedNumber", ignore = true)
    Round toDomainModel(RoundDocument document);

    RoundDocument toDocument(final Round domainModel);

}
