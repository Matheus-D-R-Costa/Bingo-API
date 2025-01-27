package com.dio.bingoapi.shared.exception;

public class BingoApiException extends RuntimeException {

    public BingoApiException(String message) {
        super(message);
    }

    public BingoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
