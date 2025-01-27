package com.dio.bingoapi.domain.player.dto;

import com.dio.bingoapi.domain.player.entity.Player;
import lombok.Builder;

import java.util.List;

public record PagedPlayersDTO(Long currentPage,
                              Long totalPages,
                              Long totalItems,
                              List<Player> content) {

    @Builder
    public PagedPlayersDTO { }

}
