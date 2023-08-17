package com.example.reactive.reactive.chapter06.ws_chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFlux
class ChatWebSocketConfig {

    @Bean
    public HandlerMapping handlerMapping() {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/path", new ChatWebSocketHandler());

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        mapping.setUrlMap(map);
        mapping.setOrder(-1);

        return mapping;
    }
}
