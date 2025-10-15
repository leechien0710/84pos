package com.example.facebookinteration.config;

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
        // Kích hoạt một message broker đơn giản trong bộ nhớ.
        // Các client sẽ subscribe vào các kênh có tiền tố "/topic".
        config.enableSimpleBroker("/topic");
        // Các tin nhắn từ client đến server sẽ được định tuyến đến các @MessageMapping
        // trong các controller nếu chúng có tiền tố "/app".
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Cho phép tất cả origins (hoặc cụ thể "http://localhost:3000")
                .withSockJS();
    }
}
