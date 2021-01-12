package com.zhuhodor.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //客户端监听的前缀
//        registry.enableSimpleBroker("/topic");
//        //客户端发送消息的前缀
//        registry.setApplicationDestinationPrefixes("/timeline");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("timeline").withSockJS();
//    }
}
