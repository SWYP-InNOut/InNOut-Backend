package inandout.backend.chat.stomp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class StompChatRoomDTO {
    private String roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();
    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션

    public static StompChatRoomDTO create(String name){
        StompChatRoomDTO room = new StompChatRoomDTO();

        room.roomId = UUID.randomUUID().toString();
        return room;
    }
}
