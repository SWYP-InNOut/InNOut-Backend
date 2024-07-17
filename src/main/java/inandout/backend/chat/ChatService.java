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
public class ChatService {

    private final ChatRepository chatRepository;

    public List<ChatRoom> findAll() {
        return chatRepository.findAll();
    }

    public ChatRoom findRoomById(String roomId) {

        return chatRepository.findById(roomId);
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
    public ChatRoom createRoom() {
        System.out.println("임의의 방 생성(1,2)");
        ChatRoom chatRoom1 = new ChatRoom("1");
        ChatRoom chatRoom2 = new ChatRoom("2");

       // ChatRoom chatRoom1 = inandout.backend.chat.ChatRoom.of("1");
        chatRepository.save("1", chatRoom1);

        //ChatRoom chatRoom2 = inandout.backend.chat.ChatRoom.of("2");
        chatRepository.save("2", chatRoom2);

        // 걍 리턴. 쓸모없음
        return chatRoom2;
    }
    public void handleAction(
            String roomId,
            WebSocketSession session,
            ChatMessageDTO chatMessage
    ) {

        ChatRoom room = findRoomById(roomId);

        // 조인은 창 열었을때
//        room.join(session);

        chatMessage.setMessage(chatMessage.getMessage());

        TextMessage textMessage = Util.Chat.resolveTextMessage(chatMessage);

        room.sendMessage(textMessage);


    }

    private boolean isEnterRoom(ChatMessageDTO chatMessage) {
        return chatMessage.getMessageType().equals(ChatMessageDTO.MessageType.ENTER);
    }

}
