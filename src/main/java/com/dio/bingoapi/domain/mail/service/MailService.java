package com.dio.bingoapi.domain.mail.service;

import com.dio.bingoapi.domain.mail.dto.MailMessageDTO;
import com.dio.bingoapi.domain.mail.gateway.MailGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MailService {

    private final MailGateway mailGateway;

    public Mono<Void> send(MailMessageDTO mailMessageDTO) {
        return mailGateway.send(mailMessageDTO);
    }

}
