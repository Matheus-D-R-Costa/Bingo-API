package com.dio.bingoapi.infrastructure.external.mail.adapter;

import com.dio.bingoapi.domain.mail.dto.MailMessageDTO;
import com.dio.bingoapi.domain.mail.gateway.MailGateway;
import com.dio.bingoapi.infrastructure.configuration.retry.RetryHelper;
import com.dio.bingoapi.shared.exception.BingoApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class MailGatewayImpl implements MailGateway {

    private final String sender;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RetryHelper retryHelper;

    @Override
    public Mono<Void> send(MailMessageDTO mailMessageDTO) {
        return Mono.just(mailSender.createMimeMessage())
                .flatMap(mimeMessage -> buildMimeMessage(mimeMessage, mailMessageDTO))
                .flatMap(this::sendWithRetry);
    }

    private Mono<Void> sendWithRetry(final MimeMessage mimeMessage) {
        return Mono.fromCallable(() -> {
                    mailSender.send(mimeMessage);
                    return mimeMessage;
                })
                .retryWhen(retryHelper.processRetry(UUID.randomUUID().toString(),
                        throwable -> throwable instanceof MailException))
                .then();
    }

    private Mono<MimeMessage> buildMimeMessage(final MimeMessage mimeMessage, final MailMessageDTO mailMessageDTO) {
        return Mono.fromCallable(() -> {
            try {
                var helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
                helper.setTo(mailMessageDTO.destination());
                helper.setFrom(sender);
                helper.setSubject(mailMessageDTO.subject());
                String body = buildMailBodyFromTemplate(mailMessageDTO.template(), mailMessageDTO.variables());
                helper.setText(body);
                return helper.getMimeMessage();
            } catch (MessagingException e) {
                throw new BingoApiException(e.getMessage(), e);
            }
        });
    }

    private String buildMailBodyFromTemplate(final String template, final Map<String, Object> variables) {
        var context = new Context(new Locale("pt", "BR"));
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

}
