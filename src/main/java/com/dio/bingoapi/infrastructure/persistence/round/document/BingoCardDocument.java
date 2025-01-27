package com.dio.bingoapi.infrastructure.persistence.round.document;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;
import java.util.List;

public record BingoCardDocument(@Field("bingo_card_id")
                                String bingoCardId,

                                @Field("player_id")
                                String playerId,

                                List<Integer> numbers,

                                @Field("hint_count")
                                Integer hintCount,

                                @Field("created_at")
                                OffsetDateTime createdAt,

                                @Field("updated_at")
                                OffsetDateTime updatedAt) {

    @Builder
    public BingoCardDocument { }

}
