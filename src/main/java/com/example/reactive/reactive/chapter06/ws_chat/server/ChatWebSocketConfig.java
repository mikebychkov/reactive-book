package com.example.reactive.reactive.chapter06.ws_chat.server;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFlux
@Log4j2
class ChatWebSocketConfig {

    @Autowired
    @Qualifier("Chat")
    private WebSocketHandler chatHandler;

    @Autowired
    @Qualifier("ChatMsg")
    private WebSocketHandler chatMsgHandler;

    @Bean
    public HandlerMapping handlerMapping() {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/chat/srv", chatHandler);
        map.put("/ws/chat/msg", chatMsgHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        mapping.setUrlMap(map);
        mapping.setOrder(-1);

        return mapping;
    }
}
