package com.example.reactive.reactive.chapter06;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableChannel;
import reactor.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class StandaloneApplication {

    public static void main(String[] args) {

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routes(new BCryptPasswordEncoder(18)));
        ReactorHttpHandlerAdapter reactorHttpHandler = new ReactorHttpHandlerAdapter(httpHandler);

        HttpServer.create()
                .port(8080)
                .handle(reactorHttpHandler)
                .bind()
                .flatMap(DisposableChannel::onDispose)
                .block();
    }

    static RouterFunction<ServerResponse> routes(PasswordEncoder passwordEncoder) {

        return route(POST("/check"),
                request -> request.bodyToMono(PasswordDTO.class)
                        .map(p -> passwordEncoder.matches(p.getRaw(), p.getSecured()))
                        .flatMap(isMatched -> isMatched
                                ? ServerResponse.ok().build()
                                : ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build())
        );
    }
}
