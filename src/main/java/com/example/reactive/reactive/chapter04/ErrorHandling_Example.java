package com.example.reactive.reactive.chapter04;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Random;

@Log4j2
public class ErrorHandling_Example {

    private final static Random random = new Random();

    public static Flux<String> recommendedBooks(String userId) {

        return Flux.defer(() -> {
            if (random.nextInt(10) < 7) {
                return Flux.<String>error(new RuntimeException("ERR"))
                        .delaySequence(Duration.ofMillis(100));
            } else {
                return Flux.just("Blue Mars", "Expansion")
                        .delayElements(Duration.ofMillis(100));
            }
        }).doOnSubscribe(s -> log.info("request for {}", userId));
    }

    public static void main(String[] args) throws InterruptedException {

        Flux.just("user-1")
                .flatMap(user -> recommendedBooks(user)
                        .retryWhen(Retry.backoff(5, Duration.ofMillis(100)))
                        .timeout(Duration.ofSeconds(3))
                        .onErrorResume(e -> Flux.just("The Martian"))
                )
                .subscribe(
                        d -> log.info("onNext: {}", d),
                        e -> log.info("onError: {}", e, e),
                        () -> log.info("onComplete")
                );
        Thread.sleep(3000);
    }
}
