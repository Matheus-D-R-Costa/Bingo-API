package com.dio.bingoapi.domain.player.dto;

import com.dio.bingoapi.domain.player.enums.PlayerSortBy;
import com.dio.bingoapi.shared.enums.SortDirection;
import lombok.Builder;

import static com.dio.bingoapi.domain.player.enums.PlayerSortBy.NAME;
import static com.dio.bingoapi.shared.enums.SortDirection.ASC;

public record PageablePlayersDTO(String sentence,
                                 Long page,
                                 Integer limit,
                                 PlayerSortBy sortBy,
                                 SortDirection sortDirection) {

    @Builder
    public PageablePlayersDTO {
        sentence = (sentence != null ? sentence : "");
        page = (page != null ? page : 1L);
        limit = (limit != null ? limit : 20);
        sortBy = (sortBy != null ? sortBy : NAME);
        sortDirection = (sortDirection != null ? sortDirection : ASC);
    }

    public Long getSkip() {
        return page > 1 ? (page - 1) * limit : 0;
    }

}
