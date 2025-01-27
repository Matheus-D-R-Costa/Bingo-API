package com.dio.bingoapi.domain.round.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class BingoCardAlreadyExistsException extends BingoApiException {

    public BingoCardAlreadyExistsException(String message) {
        super(message);
    }

}
