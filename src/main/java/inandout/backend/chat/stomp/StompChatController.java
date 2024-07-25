package inandout.backend.chat.stomp;

import inandout.backend.dto.chat.ChatMessageDTO;
import inandout.backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"

    @Autowired
    public ChatService chatService;

    @MessageMapping(value = "/chat/enter")
    public void enter(StompChatMessageDTO message){
        message.setChatContent(message.getSender() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(StompChatMessageDTO message) throws Exception {
        template.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message.getChatContent());
        System.out.println("채팅 저장");
        chatService.saveChat(message);
        System.out.println("");
    }

//    @MessageMapping(value = "/chat/enter")
//    public void enter(StompChatMessageDTO message){
//        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
//        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
//
//    @MessageMapping(value = "/chat/message")
//    public void message(StompChatMessageDTO message){
//        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message.getMessage());
//        System.out.println("");
//    }
}
