package inandout.backend.chat;

import inandout.backend.dto.chat.ChatMessageDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.*;


@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(Integer roomId) {

        return chatRoomRepository.findById(roomId);
    }

//    public ChatRoom createRoom(int memberId) {
//        String roomId = UUID.randomUUID().toString();
//
//        ChatRoom chatRoom = ChatRoom.of(roomId, memberId);
//        chatRepository.save(roomId, chatRoom);
//        // DB에 저장
//
//        return chatRoom;
//    }

    //임의로 룸 생성
    public void createRoom() {

    }
    public void handleAction(
            Integer roomId,
            WebSocketSession session,
            ChatMessageDTO chatMessage
    ) {

        ChatRoom room = findRoomById(roomId);

        // 조인은 창 열었을때
//        room.join(session);

        TextMessage textMessage = Util.Chat.resolveTextMessage(chatMessage);

        room.sendMessage(textMessage);

    }

//    private boolean isEnterRoom(ChatMessageDTO chatMessage) {
//        return chatMessage.getMessageType().equals(ChatMessageDTO.MessageType.ENTER);
//    }

}
