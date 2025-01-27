package com.dio.bingoapi.infrastructure.configuration.exception;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private static final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_NOT_FOUND = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_BAD_REQUEST = new BaseErrorMessage("generic.badRequest");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOWED = new BaseErrorMessage("generic.methodNotAllowed");
    public static final BaseErrorMessage GENERIC_MAX_RECURSION = new BaseErrorMessage("generic.maxRecursion");
    public static final BaseErrorMessage GENERIC_MAX_RETRIES = new BaseErrorMessage("generic.maxRetries");
    public static final BaseErrorMessage PLAYER_NOT_FOUND_WITH_ID = new BaseErrorMessage("player.notFoundWithId");
    public static final BaseErrorMessage PLAYER_NOT_FOUND_WITH_EMAIL = new BaseErrorMessage("player.notFoundWithEmail");
    public static final BaseErrorMessage PLAYER_EMAIL_ALREADY_USED = new BaseErrorMessage("player.emailAlreadyUsed");
    public static final BaseErrorMessage ROUND_ALREADY_STARTED = new BaseErrorMessage("round.alreadyStarted");
    public static final BaseErrorMessage ROUND_ALREADY_FINISHED = new BaseErrorMessage("round.alreadyFinished");
    public static final BaseErrorMessage ROUND_NOT_FOUND = new BaseErrorMessage("round.notFound");
    public static final BaseErrorMessage ROUND_NOT_STARTED = new BaseErrorMessage("round.notStarted");
    public static final BaseErrorMessage BINGO_CARD_ALREADY_EXISTS = new BaseErrorMessage("bingoCard.alreadyExists");


    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... PARAMS) {
        this.params = (PARAMS != null) ? PARAMS.clone() : null;
        return this;
    }

    public String getMessage() {
        String message = tryGetMessageFromBundle();
        if (hasParams()) {
            MessageFormat fmt = new MessageFormat(message);
            message = fmt.format(this.params);
        }

        return message;

    }

    private boolean hasParams() {
        return this.params != null && this.params.length > 0;
    }

    private String tryGetMessageFromBundle() {
        return getResource().getString(key);
    }

    public ResourceBundle getResource() {
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}
