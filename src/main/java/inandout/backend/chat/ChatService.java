package inandout.backend.chat;

import inandout.backend.dto.chat.ChatMessageDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.*;


@Slf4j
@Data
@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public List<ChatRoom> findAll() {
        return chatRepository.findAll();
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRepository.findById(roomId);
    }

    public ChatRoom createRoom(int memberId) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.of(roomId, memberId);
        chatRepository.save(roomId, chatRoom);
        // DB에 저장

        return chatRoom;
    }

    public void handleAction(
            String roomId,
            WebSocketSession session,
            ChatMessageDTO chatMessage
    ) {
        ChatRoom room = findRoomById(roomId);

        //방에 입장하는 코드
        if (isEnterRoom(chatMessage)) {
            room.join(session);
            chatMessage.setMessage(chatMessage.getSender() + "님 환영합니다.");
        }

        TextMessage textMessage = Util.Chat.resolveTextMessage(chatMessage);
        room.sendMessage(textMessage);
    }

    private boolean isEnterRoom(ChatMessageDTO chatMessage) {
        return chatMessage.getMessageType().equals(ChatMessageDTO.MessageType.ENTER);
    }

}
