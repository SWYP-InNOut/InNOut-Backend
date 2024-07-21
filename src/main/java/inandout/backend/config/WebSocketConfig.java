package inandout.backend.config;

import inandout.backend.chat.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

//@RequiredArgsConstructor
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final WebSocketChatHandler webSocketChatHandler;
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        System.out.println("WebSocketConfig/registerWebSocketHandlers");
//        registry.addHandler(webSocketChatHandler,"/ws/chat").setAllowedOrigins("*");
//    }
//}
