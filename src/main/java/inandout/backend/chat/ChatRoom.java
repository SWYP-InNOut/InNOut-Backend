package inandout.backend.chat;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private String roomId; // 채팅방 아이디
    private String name; // 채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();
    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }



}
