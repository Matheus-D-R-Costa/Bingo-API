package com.dio.bingoapi.shared.exception;

public class RetryException extends BingoApiException {

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

}
