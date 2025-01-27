package com.dio.bingoapi.infrastructure.configuration.retry;

import com.dio.bingoapi.shared.exception.RetryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.util.function.Predicate;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.GENERIC_MAX_RETRIES;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryHelper {

    private final RetryConfig retryConfig;

    public Retry processRetry(String retryIdentifier, Predicate<? super Throwable> errorFilter) {
        return Retry.backoff(retryConfig.maxRetries(), retryConfig.minDurationSeconds())
                .filter(errorFilter)
                .doBeforeRetry(retrySignal -> log.warn("==== Retrying {} - {} times ====", retryIdentifier,
                        retrySignal.totalRetries()))
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new RetryException(
                        GENERIC_MAX_RETRIES.getMessage(), retrySignal.failure())));
    }

}
