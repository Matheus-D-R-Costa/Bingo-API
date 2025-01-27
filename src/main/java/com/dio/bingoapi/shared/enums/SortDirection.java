package com.dio.bingoapi.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortDirection {

    ASC("ASC"),
    DESC("DESC");

    private final String direction;

}
