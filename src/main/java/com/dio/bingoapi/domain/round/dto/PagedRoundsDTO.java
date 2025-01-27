package com.dio.bingoapi.domain.round.dto;

import com.dio.bingoapi.domain.round.entity.Round;
import lombok.Builder;

import java.util.List;

public record PagedRoundsDTO(Long currentPage,
                             Long totalPages,
                             Long totalItems,
                             List<Round> content) {

    @Builder
    public PagedRoundsDTO { }

}
