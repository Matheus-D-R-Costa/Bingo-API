package com.dio.bingoapi.application.round.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record SortedNumberResponseDTO(@JsonProperty("sortedNumber")
                                   Integer sortedNumber) {

    @Builder
    public SortedNumberResponseDTO { }

}
