package com.example.reactive.reactive.chapter06.ws_chat;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
public class ChatWebSocketClient {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        WebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create("http://localhost:8080/ws/path"),
                session -> Flux.just("Hello", "Reactive", "WebSocket")
                        .map(session::textMessage)
                        .as(session::send)
                        .thenMany(session.receive())
                        .map(WebSocketMessage::getPayloadAsText)
                        .doOnEach(message -> log.info("CHAT MSG: {}", message))
                        .then()
                        .doOnTerminate(() -> log.info("CONNECTION TERMINATED"))
        ).block();
    }
}

// https://hantsy.github.io/spring-reactive-sample/web/websocket.html