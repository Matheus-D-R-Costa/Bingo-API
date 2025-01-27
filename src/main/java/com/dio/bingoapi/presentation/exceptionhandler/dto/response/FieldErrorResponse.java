package com.dio.bingoapi.presentation.exceptionhandler.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record FieldErrorResponse(@JsonProperty("name")
                                 String name,

                                 @JsonProperty("message")
                                 String message) {

    @Builder
    public FieldErrorResponse { }

}
