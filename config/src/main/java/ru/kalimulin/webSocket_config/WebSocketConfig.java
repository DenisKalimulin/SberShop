package ru.kalimulin.webSocket_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic для широковещательной рассылки
        config.enableSimpleBroker("/topic");
        // Префикс для сообщений от клиента
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Точка входа для SockJS/WebSocket
        registry
                .addEndpoint("/ws-chat")
                .addInterceptors(new CustomHandshakeInterceptor()) // Подключаем перехватчик
                .setAllowedOrigins("*");
    }
}

