package inandout.backend.service.myroom;

import inandout.backend.common.exception.BaseException;
import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.dto.myroom.*;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
import inandout.backend.repository.chat.ChatRoomJPARepository;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.myroom.MyRoomRepository;
import inandout.backend.repository.post.PostImageJPARepository;
import inandout.backend.repository.post.PostJPARepository;
import inandout.backend.repository.post.PostRepository;
import inandout.backend.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Service
@Slf4j
public class MyRoomService {

    @Autowired
    public MyRoomRepository myRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChatService chatService;
    @Autowired
    private PostJPARepository postJPARepository;
    @Autowired
    private ChatRoomJPARepository chatRoomJPARepository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private PostImageJPARepository postImageJPARepository;



    public MyRoomResponseDTO getMyRoomInfo(MyRoomRequestDTO myRoomRequestDTO) {
        Integer memberId = myRoomRequestDTO.getOwnerId();
        //memberId로 memberName 얻기
        Optional<Member> member = memberRepository.findById(memberId);
        String memberName = member.get().getName();

        // getTotalChat 로 채팅 가져오기 (일단 상위 5개만 우선적으로)
        List<ChatResponseDTO> chatResponseDTOS = chatService.getTotalChat(memberId);
        List<ChatResponseDTO> chatResponseDTOList;
        if(chatResponseDTOS.size() > 5){
            chatResponseDTOList = chatResponseDTOS.subList(0, 5);
        }else{
            chatResponseDTOList = chatResponseDTOS;
        }


        // 게시물 id 가져오기
        List<Integer> postIdList = new ArrayList<>();
        Integer fillterType = myRoomRequestDTO.getFilterType();
        switch (fillterType) {
            case 0: // 최신순
                postIdList =  postRepository.getPostIdsOrderByLatest(memberId);
                break;
            case 1: // in 많은순
                postIdList = postRepository.getPostIdsOrderByIn(memberId);
                break;
            case 2: //out 많은순
                postIdList = postRepository.getPostIdsOrderByOut(memberId);
                break;
            case 3: // 오래된순
                postIdList = postRepository.getPostIdsOrderByOldest(memberId);
                break;
            default:
                throw new BaseException(BAD_REQUEST);

        }

        // 게시물 id로 정보 가져와서 DTO에 추가
        List<MyRoomPostDTO> myRoomPostDTOList = new ArrayList<>();
        for (Integer postId : postIdList) {
            myRoomPostDTOList.add(getMyRoomPostDTO(postId));
        }


        //리턴할 DTO 만들기
        MyRoomResponseDTO myRoomResponseDTO = new MyRoomResponseDTO();
        myRoomResponseDTO.setMemberName(memberName);
        myRoomResponseDTO.setChats(chatResponseDTOList);
        myRoomResponseDTO.setPosts(myRoomPostDTOList);


        return myRoomResponseDTO;
    }

    public MyRoomPostDTO getMyRoomPostDTO(Integer postId) {
        MyRoomPostDTO myRoomPostDTO = new MyRoomPostDTO();

        Post post = postRepository.getPostByPostId(postId);
        myRoomPostDTO.setPostId(post.getId());
        myRoomPostDTO.setImgUrl(postRepository.getOldestPostImage(postId));


        return myRoomPostDTO;
    }


    public void plusUserCount(MyRoomRequestDTO myRoomRequestDTO) {
        System.out.println("plusUserCount");
        Integer memberId = myRoomRequestDTO.getOwnerId();
        Optional<Member> member = memberRepository.findById(memberId);
        Integer userCount = member.get().getUserCount();
      //  member.get().setUserCount(userCount+1);

        memberRepository.updateUserCount(memberId, userCount + 1);
        //memberRepository.save(member.get());
    }

    public MyRoomAddStuffResponseDTO addStuff(MyRoomAddStuffRequestDTO myRoomAddStuffRequestDTO, List<MultipartFile> multipartFile) {
        System.out.println("addStuff");

        //Member 찾기
        Optional<Member> member = memberRepository.findById(myRoomAddStuffRequestDTO.getMemberId());

        //myRoomAddStuffRequestDTO 요소 가져오기
        String title = myRoomAddStuffRequestDTO.getTitle();
        String outContent = myRoomAddStuffRequestDTO.getOutContent();
        String inContent = myRoomAddStuffRequestDTO.getInContent();

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(member.get());
        chatRoomJPARepository.save(chatRoom);
        MyRoomAddStuffResponseDTO myRoomAddStuffResponseDTO = new MyRoomAddStuffResponseDTO(chatRoom.getId());

        // s3에 이미지 저장
        List<String> imageUrls = s3Service.uploadFile(multipartFile);

        //post 객체 생성
        Post post = new Post(member.get(), title, outContent, inContent, 0, 0, currentDateTime, currentDateTime, chatRoom);
        postJPARepository.save(post);

        // url을 DB에 저장
        for (String imageUrl : imageUrls) {
            PostImage postImage = new PostImage(post, imageUrl, currentDateTime, currentDateTime);
            postImageJPARepository.save(postImage);
        }

        return myRoomAddStuffResponseDTO;
    }



}
