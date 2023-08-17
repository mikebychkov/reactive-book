package com.example.reactive.reactive.chapter06.ws_chat.server;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class SinkServiceImpl implements SinkService {

    private final Sinks.Many<String> chatHistory = Sinks.many().replay().limit(1000);

    @Override
    public void post(String message) {

        chatHistory.tryEmitNext(message);
    }

    @Override
    public Flux<String> history() {

        return chatHistory.asFlux();
    }
}
