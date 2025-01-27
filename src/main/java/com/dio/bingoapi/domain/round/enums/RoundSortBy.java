package com.dio.bingoapi.domain.round.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoundSortBy {

    STATE("state"),
    CREATE_DATE("created_at");

    private final String field;

}
