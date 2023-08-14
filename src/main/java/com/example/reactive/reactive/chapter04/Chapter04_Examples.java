package com.example.reactive.reactive.chapter04;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

@Log4j2
public class Chapter04_Examples {

    public static void main(String[] args) throws InterruptedException {

        // DISPOSABLE

//        Disposable disposable = Flux.interval(Duration.ofMillis(200)).subscribe(data -> log.info(data));
//        Thread.sleep(1000);
//        disposable.dispose();

        // TIMESTAMP

//        Flux.range(2023, 10).timestamp().subscribe(log::info);

        // SLIDING AVERAGE

//        int bucketSize = 5;
//        Flux.range(1, 500)
//                .index()
//                .scan(new int[bucketSize], (acc, el) -> {
//                    acc[(int)(el.getT1() % bucketSize)] = el.getT2();
//                    return acc;
//                })
//                .skip(5)
//                .map(arr -> Arrays.stream(arr).sum() * 1.0 / bucketSize)
//                .subscribe(log::info);

        // THEN MANY

//        Flux.just(1, 2, 3)
//                .thenMany(Flux.just(4, 5))
//                .subscribe(log::info);

        // BUFFER

//        Flux.range(1, 13)
//                .buffer(4)
//                .subscribe(log::info);

        // WINDOWING

//        Flux<Flux<Integer>> windowedFlux = Flux.range(101, 20)
//                .windowUntil(Chapter04_Examples::isPrime, true);
//
//        windowedFlux.subscribe(window -> window.collectList().subscribe(log::info));

        // GROUPING BY

//        Flux.range(1, 7)
//                .groupBy(i -> i % 2 == 0 ? "Even" : "Odd")
//                .subscribe(groupFlux -> groupFlux
//                        .scan(new LinkedList<>(), (list, i) -> {
//                            list.add(i);
//                            if (list.size() > 2) list.poll();
//                            return list;
//                        })
//                        .subscribe(data -> log.info("{} : {}", groupFlux.key(), data)));

        // FLAT MAP

//        Flux.just("user-1", "user-2", "user-3")
//                .flatMap(u -> userBooks(u).map(b -> u + "/" + b))
//                .subscribe(log::info);
//        Thread.sleep(1000);

        // ON EACH (SIGNAL)

//        Flux.just(1,2,3)
//                .concatWith(Flux.error(new RuntimeException("Conn exception")))
//                .doOnEach(s -> log.info("signal: {}", s))
//                .subscribe(log::info);

        // FIBONAСCI

        Flux.generate(
                    () -> Tuples.of(0L, 1L),
                    (state, sink) -> {
                        sink.next(state.getT2());
                        long nextVal = state.getT1() + state.getT2();
                        return Tuples.of(state.getT2(), nextVal);
                    }
                )
                .delayElements(Duration.ofMillis(10))
                .take(7)
                .subscribe(log::info);
        Thread.sleep(1000);
    }

    public static Random random = new Random();

    public static Flux<String> userBooks(String user) {

        return Flux.range(1, random.nextInt(3) + 1)
                .map(i -> "Book-" + i)
                .delayElements(Duration.ofMillis(10));
    }

    public static boolean isPrime(int i) {

        return Flux.just(2, 3, 5, 7)
                .filter(n -> n != i)
                .all(n -> i % n != 0)
                .map(Optional::ofNullable)
                .block()
                .orElse(false);
    }
}
