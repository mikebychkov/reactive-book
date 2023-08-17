package com.example.reactive.reactive.chapter06.ws_chat.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;

@Log4j2
public class ChatWebSocketClient {

    public static void main(String[] args) throws URISyntaxException, InterruptedException, ExecutionException {

        WebSocketClient client = new ReactorNettyWebSocketClient();

        ExecutorService pool = Executors.newSingleThreadExecutor();

        pool.submit(() ->
            client.execute(
                URI.create("http://localhost:8080/ws/chat/msg"),
                session -> session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .doOnNext(message -> log.info("CHAT MSG: {}", message))
                        .then()
                        .doOnTerminate(() -> log.info("CONNECTION TERMINATED"))
                        .doOnEach(log::error)
            ).block()
        );

        Scanner sc = new Scanner(System.in);

        while (true) {

            String message = sc.nextLine();

            if ("exit".equals(message)) break;

            client.execute(
                    URI.create("http://localhost:8080/ws/chat/srv"),
                    session -> Mono.just(message)
                            .map(session::textMessage)
                            .as(session::send)
            ).block(Duration.ofSeconds(1));
        }

        pool.shutdownNow();
    }
}
