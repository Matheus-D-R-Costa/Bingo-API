package com.dio.bingoapi.application.player.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record PlayerRequestDTO(@JsonProperty("name")
                               @NotBlank
                               @Size(min = 3, max = 150)
                               String name,

                               @JsonProperty("email")
                               @Email
                               @NotBlank
                               @Size(min = 3, max = 150)
                               String email) {

    @Builder
    public PlayerRequestDTO { }

}
