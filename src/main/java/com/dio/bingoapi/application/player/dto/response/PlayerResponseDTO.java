package com.dio.bingoapi.application.player.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record PlayerResponseDTO(@JsonProperty("id")
                                String id,

                                @JsonProperty("name")
                                String name,

                                @JsonProperty("email")
                                String email) {

    @Builder
    public PlayerResponseDTO { }

}
