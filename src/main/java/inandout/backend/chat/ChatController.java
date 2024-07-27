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
    public ResponseEntity<List<ChatResponseDTO>> getMyRoomChat(@RequestParam(name = "memberId") Integer memberId) {
        System.out.println("ChatController/getMyRoomChat");

        //전체 채팅 조회
        List<ChatResponseDTO> chatResponseDTOList = chatService.getTotalChat(memberId);

        return ResponseEntity.ok(chatResponseDTOList);
    }

    @GetMapping("/myroom/detail/{postId}/chat")
    public ResponseEntity<List<ChatResponseDTO>> getPostChat(@PathVariable(name = "postId") Integer postId,
                                                                 @RequestParam(name = "memberId") Integer memberId) {
        System.out.println("ChatController/getPostChat");

       // Integer postId =2;
        //게시물 채팅조회
        List<ChatResponseDTO> chatResponseDTOList = chatService.getPostChat(memberId, postId);

        return ResponseEntity.ok(chatResponseDTOList);
    }

    @GetMapping("/others/room/detail/{postId}/chat")
    public ResponseEntity<List<ChatResponseDTO>> getOtherPostChat(@PathVariable(name = "postId") Integer postId) {
        System.out.println("ChatController/getOtherPostChat");

        //게시물 채팅조회
        List<ChatResponseDTO> chatResponseDTOList = chatService.getOtherPostChat(postId);

        return ResponseEntity.ok(chatResponseDTOList);
    }



    // 룸 생성 관련
//    @PostMapping("/chat")
//    public ChatRoom createRoom(@RequestParam(value="memberId") int memberId) {
//        System.out.println("ChatController/createRoom");
//        return chatRoomService.createRoom();
//    }

    @GetMapping("/chat")
    public List<ChatRoom> getAll() {
        System.out.println("ChatController/getAll");
        return chatRoomService.findAll();
    }

}
