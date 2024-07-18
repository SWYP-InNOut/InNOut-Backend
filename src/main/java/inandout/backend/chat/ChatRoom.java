package inandout.backend.chat;

import inandout.backend.dto.chat.ChatMessageDTO;
import lombok.Builder;
import lombok.Data;

import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
public class ChatRoom {
    private final String roomId;
  //  private final int memberId;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId) {
        this.roomId = roomId;
    //    this.memberId = memberId;
    }

    public void sendMessage(TextMessage message) {
        System.out.println("ChatRoom/sendMessage");
        this.getSessions()
                .parallelStream()
                .forEach(session -> sendMessageToSession(session, message));
    }

    private void sendMessageToSession(WebSocketSession session, TextMessage message) {
        System.out.println("ChatRoom/sendMessageToSession");
        System.out.println("session: "+session);
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void join(WebSocketSession session) {
        System.out.println("ChatRoom/join");
        sessions.add(session);
    }

    public static ChatRoom of(String roomId) {
        return ChatRoom.builder()
                .roomId(roomId)
                //.memberId(name)
                .build();
    }

}
