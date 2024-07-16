package inandout.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer {

   private final WebSocketHandler webSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler,"/ws/chat").setAllowedOrigins("*");
    }

//    @Override
//    public void registerStompEndpoints(final StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat");
//        registry.addEndpoint("/chat").withSockJS();
//    }

//    @Override
//    public void configureMessageBroker(final MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/listen");
//        //registry.setApplicationDestinationPrefixes();
//    }
}
