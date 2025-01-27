package com.dio.bingoapi.application.player.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public record PagedPlayersResponseDTO(@JsonProperty("currentPage")
                                      Long currentPage,

                                      @JsonProperty("totalPages")
                                      Long totalPages,

                                      @JsonProperty("totalItems")
                                      Long totalItems,

                                      @JsonProperty("content")
                                      List<PlayerResponseDTO> content) {

    @Builder
    public PagedPlayersResponseDTO { }

}
