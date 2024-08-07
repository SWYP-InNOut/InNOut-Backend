package inandout.backend.controller.myroom;

import inandout.backend.argumentresolver.MemberId;
import inandout.backend.chat.ChatRoomService;
import inandout.backend.chat.stomp.StompChatRoomRepository;
import inandout.backend.common.exception.BaseException;
import inandout.backend.common.response.BaseErrorResponse;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.myroom.*;
import inandout.backend.dto.post.PostIdRequest;
import inandout.backend.jwt.JWTUtil;
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
    public final JWTUtil jwtUtil;
    @PostMapping("/myroom")
    public BaseResponse<MyRoomResponseDTO> myRoomController(@RequestBody MyRoomRequestDTO myRoomRequestDTO) {

        MyRoomResponseDTO myRoomResponseDTO = myRoomService.getMyRoomInfo(myRoomRequestDTO);

//        myRoomService.plusUserCount(myRoomRequestDTO);

        return new BaseResponse<>(myRoomResponseDTO);
    }

    @PostMapping("/myroom/addstuff")
    public BaseResponse<MyRoomAddStuffResponseDTO> myRoomAddStuffController(@RequestPart(value = "request") MyRoomAddStuffRequestDTO myRoomAddStuffRequestDTO,
                                                                              @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        System.out.println(myRoomAddStuffRequestDTO.getInContent());
        //null 처리
        if(myRoomAddStuffRequestDTO.getInContent() == null){
            myRoomAddStuffRequestDTO.setInContent("");
        }
        if (myRoomAddStuffRequestDTO.getOutContent() == null) {
            myRoomAddStuffRequestDTO.setOutContent("");
        }
        MyRoomAddStuffResponseDTO myRoomAddStuffResponseDTO = myRoomService.addStuff(myRoomAddStuffRequestDTO, multipartFile);

        return new BaseResponse<>(myRoomAddStuffResponseDTO);
    }

    @GetMapping("/myroom/post")
    public BaseResponse<PostResponseDTO> getPostAnonymousController(@RequestParam(value = "postId") String postId) {
        System.out.println("postId; "+Integer.parseInt(postId));
        System.out.println("MyRoomController/getPostAnonymousController");
        Integer memberId = -1;

        PostResponseDTO postResponseDTO = postService.getPost(memberId, Integer.parseInt(postId));

        //조회수 up
        postService.plusUserCount(Integer.parseInt(postId));

        return new BaseResponse<>(postResponseDTO);

    }

    @GetMapping("/myroom/post/{postId}")
    public BaseResponse<PostResponseDTO> getPostController(@PathVariable(value = "postId") Integer postId, @MemberId Integer memberId) {
        System.out.println("MyRoomController/getPostController");
        PostResponseDTO postResponseDTO = postService.getPost(memberId, postId);

        //조회수 up
        postService.plusUserCount(postId);

        return new BaseResponse<>(postResponseDTO);
    }

    @GetMapping("/myroom/link")
    public BaseResponse<LinkResponseDTO> generateLinkController(@RequestParam(value = "ownerId") Integer ownerId) {
        System.out.println("generateLinkController");
        LinkResponseDTO linkResponseDTO = postService.getLink(ownerId);

        return new BaseResponse<>(linkResponseDTO);
    }

    @GetMapping("/link")
    public BaseResponse<MyRoomAddStuffResponseDTO> getMemberIdFromAnonymousTokenContoller(@RequestParam(value = "token") String token) {
        System.out.println("getMemberIdFromAnonymousToken");


        Integer roomId = jwtUtil.getRoomId(token);
        if (roomId < 0) {
            BaseErrorResponse errorResponse = new BaseErrorResponse(new BaseErrorResponse(4000, 400, "만료된 익명사용자입니다."));
            throw new BaseException(errorResponse);
        }
        MyRoomAddStuffResponseDTO linkRoomIdResponseDTO = new MyRoomAddStuffResponseDTO(roomId);
        return new BaseResponse<>(linkRoomIdResponseDTO);
    }


}
