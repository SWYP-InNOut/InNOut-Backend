package inandout.backend.controller.myroom;

import inandout.backend.chat.ChatRoomService;
import inandout.backend.chat.stomp.StompChatRoomRepository;
import inandout.backend.dto.myroom.*;
import inandout.backend.service.myroom.MyRoomService;
import inandout.backend.service.myroom.S3Service;
import inandout.backend.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MyRoomController {

    @Autowired
    public MyRoomService myRoomService;

    @Autowired
    public S3Service s3Service;

    @Autowired
    public PostService postService;
    @Autowired
    public ChatRoomService chatRoomService;

    @PostMapping({"/myroom", "/others/room"})
    public ResponseEntity<MyRoomResponseDTO> myRoomController(@RequestBody MyRoomRequestDTO myRoomRequestDTO) {

        MyRoomResponseDTO myRoomResponseDTO = myRoomService.getMyRoomInfo(myRoomRequestDTO);

        myRoomService.plusUserCount(myRoomRequestDTO);

        return ResponseEntity.ok(myRoomResponseDTO);
    }

    @PostMapping("/myroom/addstuff")
    public ResponseEntity<MyRoomAddStuffResponseDTO> myRoomAddStuffController(@RequestPart(value = "request") MyRoomAddStuffRequestDTO myRoomAddStuffRequestDTO,
                                                                              @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        MyRoomAddStuffResponseDTO myRoomAddStuffResponseDTO = myRoomService.addStuff(myRoomAddStuffRequestDTO, multipartFile);


        return ResponseEntity.ok(myRoomAddStuffResponseDTO);
    }

    @GetMapping({"/myroom/post/{postId}","/others/post/{postId}"})
    public ResponseEntity<PostResponseDTO> getPostController(@PathVariable(value = "postId") Integer postId, @RequestParam(value = "memberId") Integer memberId) {
        PostResponseDTO postResponseDTO = postService.getPost(memberId, postId);

        return ResponseEntity.ok(postResponseDTO);
    }


}
