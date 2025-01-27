package com.dio.bingoapi.infrastructure.configuration.resources;

import com.dio.bingoapi.domain.mail.gateway.MailGateway;
import com.dio.bingoapi.domain.mail.service.MailService;
import com.dio.bingoapi.infrastructure.configuration.retry.RetryHelper;
import com.dio.bingoapi.infrastructure.external.mail.adapter.MailGatewayImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
public class MailConfig {

    @Value("${bingo-api.mail.sender}")
    private String sender;

    @Bean
    public MailGateway mailGateway(JavaMailSender mailSender, TemplateEngine templateEngine, RetryHelper retryHelper) {
        return new MailGatewayImpl(sender, mailSender, templateEngine, retryHelper);
    }

    @Bean
    public MailService mailService(MailGateway mailGateway) {
        return new MailService(mailGateway);
    }

}
