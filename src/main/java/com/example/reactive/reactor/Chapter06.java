package com.example.reactive.reactor;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.util.LinkedList;
import java.util.Optional;

@Log4j2
public class Chapter06 {

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
//                .windowUntil(Chapter06::isPrime, true);
//
//        windowedFlux.subscribe(window -> window.collectList().subscribe(log::info));

        // GROUPING BY

        Flux.range(1, 7)
                .groupBy(i -> i % 2 == 0 ? "Even" : "Odd")
                .subscribe(groupFlux -> groupFlux
                        .scan(new LinkedList<>(), (list, i) -> {
                            list.add(i);
                            if (list.size() > 2) list.poll();
                            return list;
                        })
                        .subscribe(data -> log.info("{} : {}", groupFlux.key(), data)));
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
