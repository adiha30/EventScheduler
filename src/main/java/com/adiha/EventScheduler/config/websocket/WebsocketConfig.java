package com.adiha.EventScheduler.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.adiha.EventScheduler.utils.Constants.*;

/**
 * This class is a configuration class for WebSocket communication.
 * It implements the WebSocketMessageBrokerConfigurer interface to provide WebSocket configuration.
 * It is annotated with @Configuration and @EnableWebSocketMessageBroker to indicate that it is a configuration class and to enable WebSocket message handling.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * This method is used to register STOMP endpoints.
     * It adds a STOMP endpoint at the "/ws" URL path and enables SockJS fallback options.
     *
     * @param registry The STOMP endpoint registry.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEB_SOCKET_ENDPOINT).withSockJS();
    }

    /**
     * This method is used to configure the message broker.
     * It sets the application destination prefixes to "/app" and enables a simple message broker at the "/topic" URL path.
     *
     * @param registry The message broker registry.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(WEBSOCKET_PREFIX).enableSimpleBroker(TOPICS_DEST_PREFIX);
    }
}