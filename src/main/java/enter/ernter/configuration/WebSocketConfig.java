package enter.ernter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import enter.ernter.handler.CustomWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

        private final CustomWebSocketHandler handler;

        public WebSocketConfig(CustomWebSocketHandler handler) {
                this.handler = handler;
        }

        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                registry.addHandler(handler, "/ws")
                .setAllowedOrigins("*");
        }
}

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//         private final CustomWebSocketHandler webSocketHandler;

//         public WebSocketConfig(CustomWebSocketHandler webSocketHandler) {
//                 this.webSocketHandler = webSocketHandler;
//         }

//         @Override
//         public void configureMessageBroker(MessageBrokerRegistry config) {
//                 config.enableSimpleBroker("/topic"); // Prefix for broadcasting messages
//                 config.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server communication
//         }

//         @Override
//         public void registerStompEndpoints(StompEndpointRegistry registry) {
//                 registry.addEndpoint("/ws")
//                                 .setAllowedOriginPatterns("*")
//                                 // .addInterceptors(new WebSocketHandshakeInterceptor())
//                                 .withSockJS()
//                                 ;
//         }

// }