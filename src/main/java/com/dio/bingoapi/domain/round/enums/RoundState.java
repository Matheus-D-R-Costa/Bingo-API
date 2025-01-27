package com.dio.bingoapi.domain.round.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoundState {

    CREATED("created"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished");

    private final String description;

}
