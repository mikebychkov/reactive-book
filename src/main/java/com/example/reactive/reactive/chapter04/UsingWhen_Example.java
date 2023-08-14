package com.example.reactive.reactive.chapter04;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Log4j2
public class UsingWhen_Example {

    public static class Transaction {

        private final static Random random = new Random();
        private final int id;

        public Transaction(int id) {
            this.id = id;
            log.info("[T: {}] created", id);
        }

        public static Mono<Transaction> beginTransaction() {

            return Mono.defer(() -> Mono.just(
                            new Transaction(random.nextInt(1000))
                    ));
        }

        public Flux<String> insertRows(Publisher<String> rows) {

            return Flux.from(rows)
                    .delayElements(Duration.ofMillis(100))
                    .flatMap(r -> {
                        if (random.nextInt(10) < 2) {
                            return Mono.error(new RuntimeException("Error on row: " + r));
                        } else {
                            return Mono.just(r);
                        }
                    });
        }

        public Mono<Void> commit() {

            return Mono.defer(() -> {
                log.info("[T: {}] commit", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conn error"));
                }
            });
        }

        public Mono<Void> rollback() {

            return Mono.defer(() -> {
                log.info("[T: {}] rollback", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conn error"));
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Flux.usingWhen(
                Transaction.beginTransaction(),
                tr -> tr.insertRows(Flux.just("A", "B", "C")),
                Transaction::commit,
                (tr, ex) -> tr.rollback(),
                Transaction::rollback
        )
        .subscribe(
            d -> log.info("onNext: {}", d),
            e -> log.info("onError: {}", e, e),
            () -> log.info("onComplete")
        );
        Thread.sleep(1000);
    }
}
