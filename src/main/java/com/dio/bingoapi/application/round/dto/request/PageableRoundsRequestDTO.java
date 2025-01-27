package com.dio.bingoapi.application.round.dto.request;

import com.dio.bingoapi.domain.round.enums.RoundSortBy;
import com.dio.bingoapi.shared.enums.SortDirection;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.OffsetDateTime;

import static com.dio.bingoapi.domain.round.enums.RoundSortBy.CREATE_DATE;
import static com.dio.bingoapi.shared.enums.SortDirection.DESC;

public record PageableRoundsRequestDTO(@JsonProperty("sentence")
                                       String sentence,

                                       @JsonProperty("startDate")
                                       @PastOrPresent
                                       OffsetDateTime startDate,

                                       @JsonProperty("endDate")
                                       @PastOrPresent
                                       OffsetDateTime endDate,

                                       @JsonProperty("page")
                                       @PositiveOrZero
                                       Long page,


                                       @JsonProperty("limit")
                                       @Min(1)
                                       @Max(50)
                                       Integer limit,

                                       @JsonProperty("sortBy")
                                       RoundSortBy sortBy,

                                       @JsonProperty("sortDirection")
                                       SortDirection sortDirection) {

    @Builder
    public PageableRoundsRequestDTO {
        sentence = (sentence != null ? sentence : "");
        page = (page != null ? page : 1L);
        limit = (limit != null ? limit : 20);
        sortBy = (sortBy != null ? sortBy : CREATE_DATE);
        sortDirection = (sortDirection != null ? sortDirection : DESC);
    }

}
