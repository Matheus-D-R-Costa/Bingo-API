package com.dio.bingoapi.domain.round.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class RoundNotStartedException extends BingoApiException {
    public RoundNotStartedException(String message) {
        super(message);
    }
}
