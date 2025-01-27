package com.dio.bingoapi.domain.player.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerSortBy {

    NAME("name"),
    EMAIL("email");

    private final String field;

}
