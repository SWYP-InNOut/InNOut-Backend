package inandout.backend.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import inandout.backend.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("WebSocketChatHandler/handleTextMessage");
        String payload = message.getPayload();

        ChatMessageDTO chatMessage = Util.Chat.resolvePayload(payload);
        chatService.handleAction(chatMessage.getRoomId(), session, chatMessage);

    }

//    private final ObjectMapper mapper;
//
//    // 연결되어있는 세션들
//    private final Set<WebSocketSession> sessions = new HashSet<>();
//
//    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
//
//    // 소켓 연결 확인
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//
//    // 메시지 전송
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        ChatMessageDTO chatMessageDTO = mapper.readValue(payload, ChatMessageDTO.class);
//
//
//        Long chatRoomId = chatMessageDTO.getRoomId();
//        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
//        if(!chatRoomSessionMap.containsKey(chatRoomId)){
//            chatRoomSessionMap.put(chatRoomId,new HashSet<>());
//        }
//        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);
//
//        if (chatMessageDTO.getMessageType().equals(ChatMessageDTO.MessageType.ENTER)) {
//            // sessions 에 넘어온 session 을 담고,
//            chatRoomSession.add(session);
//        }
//        sendMessageToChatRoom(chatMessageDTO, chatRoomSession);
//    }
//
//    //소켓 연결 종료 확인
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
//    }
//
//
//    private void sendMessageToChatRoom(ChatMessageDTO chatMessageDto, Set<WebSocketSession> chatRoomSession) {
//        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));//2
//    }
//
//
//    public <T> void sendMessage(WebSocketSession session, T message) {
//        try{
//            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
}
