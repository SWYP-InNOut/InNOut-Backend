package inandout.backend.chat;


import inandout.backend.dto.chat.ChatResponseDTO;

import inandout.backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {
   // private final ChatingService chatingService;

    @Autowired
    public ChatService chatService;

    @Autowired
    public ChatRoomService chatRoomService;



    @GetMapping("/myroom/chat")
    public ResponseEntity<List<ChatResponseDTO>> getMyRoomChat(@RequestParam(name = "memberId") Long memberId) {
        System.out.println("ChatController/getMyRoomChat");

        //전체 채팅 조회
        List<ChatResponseDTO> chatResponseDTOList = chatService.getTotalChat(memberId);

        return ResponseEntity.ok(chatResponseDTOList);
    }

    @GetMapping("/myroom/detail/{bulletinId}/chat")
    public ResponseEntity<List<ChatResponseDTO>> getBulletinChat(@PathVariable(name = "bulletinId") Long bulletinId,
                                                                 @RequestParam(name = "memberId") Long memberId) {
        System.out.println("ChatController/getBulletinChat");

        //전체 채팅 조회
        List<ChatResponseDTO> chatResponseDTOList = chatService.getBulletinChat(memberId);

        return ResponseEntity.ok(chatResponseDTOList);
    }


    // 룸 생성 관련
    @PostMapping("/chat")
    public ChatRoom createRoom(@RequestParam(value="memberId") int memberId) {
        System.out.println("ChatController/createRoom");
        return chatRoomService.createRoom();
    }

    @GetMapping("/chat")
    public List<ChatRoom> getAll() {
        System.out.println("ChatController/getAll");
        return chatRoomService.findAll();
    }

}
