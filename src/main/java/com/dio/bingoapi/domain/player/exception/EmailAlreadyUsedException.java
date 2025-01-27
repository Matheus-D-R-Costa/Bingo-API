package com.dio.bingoapi.domain.player.exception;

import com.dio.bingoapi.shared.exception.BingoApiException;

public class EmailAlreadyUsedException extends BingoApiException {

    public EmailAlreadyUsedException(String message) {
        super(message);
    }

}
