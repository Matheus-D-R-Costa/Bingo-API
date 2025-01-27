package com.dio.bingoapi.domain.mail.dto;

import com.dio.bingoapi.domain.player.entity.Player;
import com.dio.bingoapi.domain.round.entity.BingoCard;
import com.dio.bingoapi.domain.round.entity.Round;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MailMessageDTO(String destination,
                             String subject,
                             String template,
                             Map<String, Object> variables) {

    public static MailMessageDTO create(final Round round, final Player player, final BingoCard bingoCard) {
        return MailMessageDTO.builder()
                .create(round, player, bingoCard)
                .template("mail/roundResult")
                .build();
    }

    public static MailMessageDTO createWinner(final Round round, final Player player, final BingoCard bingoCard) {
        return MailMessageDTO.builder()
                .create(round, player, bingoCard)
                .template("mail/winnerResult")
                .build();
    }

    public static MailMessagedDTOBuilder builder() {
        return new MailMessagedDTOBuilder();
    }

    public static class MailMessagedDTOBuilder {

        private String destination;
        private String subject;
        private String template;
        private Map<String, Object> variables = new HashMap<>();

        public MailMessagedDTOBuilder create(final Round round, final Player player, final BingoCard bingoCard) {
            return this.destination(player.email())
                    .subject("Bingo API: Ganhador")
                    .playerName(player.name())
                    .roundId(round.id())
                    .cardNumbers(bingoCard.numbers())
                    .sortedNumbers(round.sortedNumbers())
                    .hintCount(bingoCard.hintCount());
        }

        public MailMessagedDTOBuilder destination(final String destination) {
            this.destination = destination;
            return this;
        }

        public MailMessagedDTOBuilder subject(final String subject) {
            this.subject = subject;
            return this;
        }

        public MailMessagedDTOBuilder template(final String template) {
            this.template = template;
            return this;
        }

        public MailMessagedDTOBuilder playerName(final String playerName) {
            return variable("playerName", playerName);
        }

        public MailMessagedDTOBuilder roundId(final String roundId) {
            return variable("roundId", roundId);
        }

        public MailMessagedDTOBuilder cardNumbers(final List<Integer> cardNumbers) {
            cardNumbers.sort(Integer::compareTo);
            String cardNumbersString = cardNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            return variable("cardNumbers", cardNumbersString);
        }

        public MailMessagedDTOBuilder sortedNumbers(final List<Integer> sortedNumbers) {
            sortedNumbers.sort(Integer::compareTo);
            String sortedNumbersString = sortedNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            return variable("sortedNumbers", sortedNumbersString);
        }

        public MailMessagedDTOBuilder hintCount(final Integer hintCount) {
            return variable("hintCount", hintCount);
        }

        public MailMessageDTO build() {
            return new MailMessageDTO(destination, subject, template, variables);
        }

        private MailMessagedDTOBuilder variable(final String key, final Object value) {
            this.variables.put(key, value);
            return this;
        }

    }

}
