package com.dio.bingoapi.application.round.mapper;

import com.dio.bingoapi.application.round.dto.response.BingoCardResponseDTO;
import com.dio.bingoapi.domain.round.entity.BingoCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BingoCardMapper {

    BingoCardResponseDTO toResponse(BingoCard entity);

}
