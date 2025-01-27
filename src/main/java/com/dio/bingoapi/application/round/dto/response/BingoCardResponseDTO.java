package com.dio.bingoapi.application.round.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public record BingoCardResponseDTO(@JsonProperty("playerId")
                                   String playerId,

                                   @JsonProperty("numbers")
                                   List<Integer> numbers,

                                   @JsonProperty("hintCount")
                                   Integer hintCount) {

    @Builder
    public BingoCardResponseDTO { }

}
