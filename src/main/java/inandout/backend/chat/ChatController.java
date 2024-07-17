package inandout.backend.chat;


import inandout.backend.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam(value="memberId") int memberId) {
        System.out.println("ChatController/createRoom");
        return chatService.createRoom(memberId);
    }

    @GetMapping
    public List<ChatRoom> getAll() {
        System.out.println("ChatController/getAll");
        return chatService.findAll();
    }

}
