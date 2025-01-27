package com.dio.bingoapi.domain.round.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class RecursionException extends BingoApiException {

    public RecursionException(String message) {
        super(message);
    }

}
