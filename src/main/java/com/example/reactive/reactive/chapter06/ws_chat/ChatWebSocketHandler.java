package com.example.reactive.reactive.chapter06.ws_chat;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicReference;

@Component
@Log4j2
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<String> chatHistory = Sinks.many().replay().limit(1000);

    @Override
    public Mono<Void> handle(WebSocketSession session) {

//        return session.receive()
//                .map(WebSocketMessage::getPayloadAsText)
//                .doOnNext(message -> {
//                    log.info("WS INBOUND MSG: {}", message);
//                    chatHistory.tryEmitNext(message);
//                })
//                .thenMany(chatHistory.asFlux())
//                .doOnNext(message -> log.info("WS OUTBOUND MSG: {}", message))
//                .map(session::textMessage)
//                .as(session::send);

        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(event -> {
                    log.info("WS INBOUND MSG: {}", event);
                    chatHistory.tryEmitNext(event);
                })
                .zipWith(
                        session.send(
                                chatHistory.asFlux()
                                        .doOnNext(message -> log.info("WS OUTBOUND MSG: {}", message))
                                        .map(session::textMessage)
                        )
                )
                .then();
    }
}
