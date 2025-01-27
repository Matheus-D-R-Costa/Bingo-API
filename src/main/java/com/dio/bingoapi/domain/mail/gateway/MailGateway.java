package com.dio.bingoapi.domain.mail.gateway;

import com.dio.bingoapi.domain.mail.dto.MailMessageDTO;
import reactor.core.publisher.Mono;

public interface MailGateway {

    Mono<Void> send(MailMessageDTO mailMessageDTO);

}
