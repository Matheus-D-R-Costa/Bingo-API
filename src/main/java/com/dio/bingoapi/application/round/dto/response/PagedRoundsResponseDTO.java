package com.dio.bingoapi.application.round.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public record PagedRoundsResponseDTO(@JsonProperty("currentPage")
                                     Long currentPage,

                                     @JsonProperty("totalPages")
                                     Long totalPages,

                                     @JsonProperty("totalItems")
                                     Long totalItems,

                                     @JsonProperty("content")
                                     List<RoundResponseDTO> content) {

    @Builder
    public PagedRoundsResponseDTO {}

}
