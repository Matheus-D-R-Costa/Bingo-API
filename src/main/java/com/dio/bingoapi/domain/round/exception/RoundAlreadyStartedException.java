package com.dio.bingoapi.domain.round.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class RoundAlreadyStartedException extends BingoApiException {

    public RoundAlreadyStartedException(String message) {
        super(message);
    }

}
