package com.example.reactive.reactive.chapter06.ws_chat.server;

import reactor.core.publisher.Flux;

public interface SinkService {

    void post(String message);

    Flux<String> history();
}
