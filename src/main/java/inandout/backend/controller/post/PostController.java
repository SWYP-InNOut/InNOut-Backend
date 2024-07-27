package inandout.backend.controller.post;

import inandout.backend.chat.stomp.StompChatRoomRepository;
import inandout.backend.dto.myroom.MyRoomRequestDTO;
import inandout.backend.dto.myroom.MyRoomResponseDTO;
import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.dto.others.InOutRequestDTO;
import inandout.backend.entity.post.Post;
import inandout.backend.service.post.PostService;
import inandout.backend.service.stuff.StuffService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    @Autowired
    public PostService postService;

    @Autowired
    public StuffService stuffService;

    @PostMapping("/in")
    public ResponseEntity inController(@RequestBody InOutRequestDTO inOutRequestDTO) throws Exception {
        log.info("in");

        //inout 테이블에 저장
        stuffService.saveIn(inOutRequestDTO);
        //post 테이블에서 in 증가
        Integer newInCount = postService.plusInCount(inOutRequestDTO.getPostId());


        return ResponseEntity.ok(newInCount);
    }

    @PostMapping("/out")
    public ResponseEntity outController(@RequestBody InOutRequestDTO inOutRequestDTO) throws Exception {
        log.info("out");
       // HttpSession session = request.getSession();
        //inout 테이블에 저장
        stuffService.saveOut(inOutRequestDTO);
        //post 테이블에서 in 증가
        Integer newOutCount = postService.plusOutCount(inOutRequestDTO.getPostId());


        return ResponseEntity.ok(newOutCount);
    }

}
