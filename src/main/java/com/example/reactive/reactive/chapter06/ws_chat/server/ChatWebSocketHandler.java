package com.example.reactive.reactive.chapter06.ws_chat.server;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component("Chat")
@Log4j2
public class ChatWebSocketHandler implements WebSocketHandler {

    @Autowired
    private SinkService sinkService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(event -> {
                    log.info("WS INBOUND MSG: {}", event);
                    sinkService.post(event);
                })
//                .zipWith(
//                        session.send(
//                                sinkService.history()
//                                        .doOnNext(message -> log.info("WS OUTBOUND MSG: {}", message))
//                                        .map(session::textMessage)
//                        )
//                )
                .then();
    }
}
