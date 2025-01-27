package com.dio.bingoapi.domain.round.dto;

import com.dio.bingoapi.domain.round.enums.RoundSortBy;
import com.dio.bingoapi.shared.enums.SortDirection;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.dio.bingoapi.domain.round.enums.RoundSortBy.CREATE_DATE;
import static com.dio.bingoapi.shared.enums.SortDirection.DESC;

public record PageableRoundsDTO(String sentence,
                                OffsetDateTime startDate,
                                OffsetDateTime endDate,
                                Long page,
                                Integer limit,
                                RoundSortBy sortBy,
                                SortDirection sortDirection) {

    @Builder
    public PageableRoundsDTO {
        sentence = (sentence != null ? sentence : "");
        startDate = (startDate != null ? startDate : OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(-3)));
        endDate = (endDate != null ? endDate : OffsetDateTime.now());
        page = (page != null ? page : 1L);
        limit = (limit != null ? limit : 20);
        sortBy = (sortBy != null ? sortBy : CREATE_DATE);
        sortDirection = (sortDirection != null ? sortDirection : DESC);
    }

    public Long getSkip() {
        return page > 1 ? (page - 1) * limit : 0;
    }

}
