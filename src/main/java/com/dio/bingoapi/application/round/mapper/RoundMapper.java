package com.dio.bingoapi.application.round.mapper;

import com.dio.bingoapi.application.round.dto.request.PageableRoundsRequestDTO;
import com.dio.bingoapi.application.round.dto.response.PagedRoundsResponseDTO;
import com.dio.bingoapi.application.round.dto.response.RoundResponseDTO;
import com.dio.bingoapi.domain.round.dto.PageableRoundsDTO;
import com.dio.bingoapi.domain.round.dto.PagedRoundsDTO;
import com.dio.bingoapi.domain.round.entity.Round;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BingoCardMapper.class})
public interface RoundMapper {

    RoundResponseDTO toResponse(Round entity);

    PagedRoundsResponseDTO toResponse(PagedRoundsDTO domaiDto);

    PageableRoundsDTO toDomainDto(PageableRoundsRequestDTO request);

}
