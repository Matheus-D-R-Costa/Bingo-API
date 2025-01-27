package com.dio.bingoapi.domain.round.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class RoundAlreadyFinishedException extends BingoApiException {

    public RoundAlreadyFinishedException(String message) {
        super(message);
    }

}
