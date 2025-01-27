package com.dio.bingoapi.presentation.exceptionhandler;

import com.dio.bingoapi.domain.player.exception.EmailAlreadyUsedException;
import com.dio.bingoapi.domain.round.exception.RecursionException;
import com.dio.bingoapi.domain.round.exception.RoundAlreadyFinishedException;
import com.dio.bingoapi.domain.round.exception.RoundAlreadyStartedException;
import com.dio.bingoapi.domain.round.exception.RoundNotStartedException;
import com.dio.bingoapi.presentation.exceptionhandler.dto.response.FieldErrorResponse;
import com.dio.bingoapi.presentation.exceptionhandler.dto.response.ProblemResponse;
import com.dio.bingoapi.shared.exception.BingoApiException;
import com.dio.bingoapi.shared.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Order(-2)
@Component
@RequiredArgsConstructor
public class exceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(RoundNotStartedException.class, e -> handleRoundNotStatedException(exchange, e))
                .onErrorResume(RoundAlreadyFinishedException.class, e -> handleRoundAlreadyFinishedException(exchange, e))
                .onErrorResume(RoundAlreadyStartedException.class, e -> handleRoundAlreadyStartedException(exchange, e))
                .onErrorResume(EmailAlreadyUsedException.class, e -> handleEmailAlreadyUsedException(exchange, e))
                .onErrorResume(NotFoundException.class, e -> handleNotFoundException(exchange, e))
                .onErrorResume(RecursionException.class, e -> handleRecursionException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> handleConstraintViolationException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> handleWebExchangeBindException(exchange, e))
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> handleResponseStatusException(exchange, e))
                .onErrorResume(BingoApiException.class, e -> handleBingoApiException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> handleJsonProcessingException(exchange, e))
                .onErrorResume(Exception.class, e -> handlerException(exchange, e))
                .then();
    }

    private Mono<Void> handleRoundNotStatedException(ServerWebExchange exchange, RoundNotStartedException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleRoundAlreadyFinishedException(ServerWebExchange exchange, RoundAlreadyFinishedException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleRoundAlreadyStartedException(ServerWebExchange exchange, RoundAlreadyStartedException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleEmailAlreadyUsedException(ServerWebExchange exchange, EmailAlreadyUsedException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleNotFoundException(ServerWebExchange exchange, NotFoundException ex) {
        return Mono.just(ProblemResponse.builder().create(NOT_FOUND.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleRecursionException(ServerWebExchange exchange, RecursionException ex) {
        return Mono.just(ProblemResponse.builder().create(LOOP_DETECTED.value(), ex.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleConstraintViolationException(ServerWebExchange exchange, ConstraintViolationException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(),
                        GENERIC_BAD_REQUEST.getMessage()).build())
                .flatMap(problemResponse -> addFieldsErrors(problemResponse, ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<ProblemResponse> addFieldsErrors(final ProblemResponse problemResponse, ConstraintViolationException ex) {
        return Flux.fromIterable(ex.getConstraintViolations())
                .map(constraintViolation -> FieldErrorResponse.builder()
                        .name(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().toString())
                        .message(constraintViolation.getMessage())
                        .build())
                .collectList()
                .map(fieldErrorResponses -> problemResponse.toBuilder().fields(fieldErrorResponses).build());
    }

    private Mono<Void> handleWebExchangeBindException(ServerWebExchange exchange, WebExchangeBindException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(),
                        GENERIC_BAD_REQUEST.getMessage()).build())
                .flatMap(problemResponse -> addFieldsErrors(problemResponse, ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<ProblemResponse> addFieldsErrors(final ProblemResponse problemResponse, WebExchangeBindException ex) {
        return Flux.fromIterable(ex.getFieldErrors())
                .map(fieldError -> FieldErrorResponse.builder()
                        .name(fieldError.getField())
                        .message(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(fieldErrorResponses -> problemResponse.toBuilder().fields(fieldErrorResponses).build());
    }

    private Mono<Void> handleMethodNotAllowedException(ServerWebExchange exchange, MethodNotAllowedException ex) {
        return Mono.just(ProblemResponse.builder().create(METHOD_NOT_ALLOWED.value(),
                        GENERIC_METHOD_NOT_ALLOWED.params(exchange.getRequest().getMethod().name())
                                .getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException ex) {
        return Mono.just(ProblemResponse.builder().create(NOT_FOUND.value(),
                        GENERIC_NOT_FOUND.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleBingoApiException(ServerWebExchange exchange, BingoApiException ex) {
        return Mono.just(ProblemResponse.builder().create(INTERNAL_SERVER_ERROR.value(),
                        GENERIC_EXCEPTION.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleJsonProcessingException(ServerWebExchange exchange, JsonProcessingException ex) {
        return Mono.just(ProblemResponse.builder().create(BAD_REQUEST.value(),
                        GENERIC_BAD_REQUEST.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handlerException(ServerWebExchange exchange, Exception ex) {
        return Mono.just(ProblemResponse.builder().create(INTERNAL_SERVER_ERROR.value(),
                        GENERIC_EXCEPTION.getMessage()).build())
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, ProblemResponse problemResponse) {
        return Mono.fromCallable(() -> {
            exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(problemResponse.status()));
            return new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(problemResponse));
        }).flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)));
    }

}
