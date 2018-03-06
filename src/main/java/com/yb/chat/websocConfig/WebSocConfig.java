package com.yb.chat.websocConfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Configuration
public class WebSocConfig {
	

    @Bean
    public HandlerMapping webSocketMapping(UnicastProcessor<MessageBean> messagePublisher,Flux<MessageBean> messages) {
        Map<String, Object> map = new HashMap<>();
        map.put("/basicChatApp", new MessageWebSocketHandler(messagePublisher,messages));
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(map);
        simpleUrlHandlerMapping.setOrder(1);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


}
