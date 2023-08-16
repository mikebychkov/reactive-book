package com.example.reactive.reactive.chapter06.ws_example;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public class MyWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    log.info("WS MSG: {}", message);
                })
                .map(message -> "WS RESPONSE" + message)
                .map(session::textMessage)
                .as(session::send);
    }
}
