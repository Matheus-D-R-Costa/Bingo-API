package com.dio.bingoapi.domain.round.entity;

import com.dio.bingoapi.domain.round.exception.RecursionException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import static com.dio.bingoapi.infrastructure.configuration.exception.BaseErrorMessage.GENERIC_MAX_RECURSION;

public record BingoCard(String id,
                        String playerId,
                        List<Integer> numbers,
                        Integer hintCount,
                        OffsetDateTime createdAt,
                        OffsetDateTime updatedAt) {

    public static BingoCardBuilder builder() {
        return new BingoCardBuilder();
    }

    public BingoCardBuilder toBuilder() {
        return new BingoCardBuilder(id, playerId, numbers, hintCount, createdAt, updatedAt);
    }

    public Mono<Boolean> isCompleted() {
        return Mono.just(hintCount >= 20);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class BingoCardBuilder {

        private static final int RECURSION_LIMIT = 50;

        private String id;
        private String playerId;
        private List<Integer> numbers = List.of();
        private Integer hintCount;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public BingoCardBuilder id(String id) {
            this.id = id;
            return this;
        }

        public BingoCardBuilder playerId(String playerId) {
            this.playerId = playerId;
            return this;
        }

        public BingoCardBuilder numbers(List<Integer> numbers) {
            this.numbers = numbers;
            return this;
        }

        public BingoCardBuilder hintCount(Integer hintCount) {
            this.hintCount = hintCount;
            return this;
        }

        public BingoCardBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BingoCardBuilder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BingoCard build() {
            return new BingoCard(id, playerId, numbers, hintCount, createdAt, updatedAt);
        }

        public Mono<BingoCardBuilder> incrementHintCountIfContains(Integer sortedNumber) {
            return Mono.defer(() -> {
                if (numbers.contains(sortedNumber)) {
                    ++this.hintCount;
                    this.updatedAt = OffsetDateTime.now();
                }

                return Mono.just(this);

            });
        }

        public Mono<BingoCardBuilder> generate(String playerId, List<BingoCard> bingoCards) {
            return generateNumbers(bingoCards, new Random(), 0)
                    .map(numbers -> BingoCard.builder().id(ObjectId.get().toString())
                            .playerId(playerId)
                            .numbers(numbers)
                            .hintCount(0)
                            .createdAt(OffsetDateTime.now())
                            .updatedAt(OffsetDateTime.now()));
        }

        private Mono<List<Integer>> generateNumbers(List<BingoCard> bingoCards, Random random, int recursionCount) {
            if (recursionCount >= RECURSION_LIMIT)
                return Mono.error(new RecursionException(GENERIC_MAX_RECURSION.getMessage()));

            return Flux.generate((SynchronousSink<Integer> sink) -> sink.next(random.nextInt(100)))
                    .distinct()
                    .take(20)
                    .collectSortedList()
                    .flatMap(sortedNumbers -> isValidNumbers(sortedNumbers, bingoCards)
                            .flatMap(invalids -> invalids
                                    ? generateNumbers(bingoCards, random, recursionCount + 1)
                                    : Mono.just(sortedNumbers)));
        }

        private Mono<Boolean> isValidNumbers(List<Integer> sortedNumbers, List<BingoCard> bingoCards) {
            return Flux.fromIterable(bingoCards)
                    .flatMap(bingoCard -> countDuplicates(sortedNumbers, bingoCard.numbers))
                    .any(duplicates -> duplicates > 5);
        }

        private Mono<Long> countDuplicates(List<Integer> sortedNumbers, List<Integer> numbersOfBingoCards) {
            return Flux.fromIterable(sortedNumbers)
                    .filter(numbersOfBingoCards::contains)
                    .count();
        }

    }

}
