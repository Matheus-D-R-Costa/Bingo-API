package com.dio.bingoapi.application.player.dto.request;

import com.dio.bingoapi.domain.player.enums.PlayerSortBy;
import com.dio.bingoapi.shared.enums.SortDirection;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import static com.dio.bingoapi.domain.player.enums.PlayerSortBy.NAME;
import static com.dio.bingoapi.shared.enums.SortDirection.ASC;

public record PageablePlayersRequestDTO(@JsonProperty("sentence")
                                        String sentence,

                                        @JsonProperty("page")
                                        @PositiveOrZero
                                        Long page,

                                        @JsonProperty("limit")
                                        @Min(1)
                                        @Max(50)
                                        Integer limit,

                                        @JsonProperty("sortBy")
                                        PlayerSortBy sortBy,

                                        @JsonProperty("sortDirection")
                                        SortDirection sortDirection) {

    @Builder
    public PageablePlayersRequestDTO {
        sentence = (sentence != null ? sentence : "");
        page = (page != null ? page : 1L);
        limit = (limit != null ? limit : 20);
        sortBy = (sortBy != null ? sortBy : NAME);
        sortDirection = (sortDirection != null ? sortDirection : ASC);
    }

}
