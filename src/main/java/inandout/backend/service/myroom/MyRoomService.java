package inandout.backend.service.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.dto.myroom.MyRoomAddStuffRequestDTO;
import inandout.backend.dto.myroom.MyRoomPostDTO;
import inandout.backend.dto.myroom.MyRoomRequestDTO;
import inandout.backend.dto.myroom.MyRoomResponseDTO;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.myroom.MyRoomRepository;
import inandout.backend.repository.post.PostJPARepository;
import inandout.backend.repository.post.PostRepository;
import inandout.backend.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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



    public MyRoomResponseDTO getMyRoomInfo(MyRoomRequestDTO myRoomRequestDTO) {
        Integer memberId = myRoomRequestDTO.getMemberId();
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
        List<Integer> postIdList = postRepository.getPostIdsByMemberId(memberId);

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
        myRoomPostDTO.setImgUrl(post.getPostImages().get(0).getPostImgUrl());  // 제일 첫번쨰것으로
        myRoomPostDTO.setCreatedAt(post.getCreatedAt());

        return myRoomPostDTO;
    }


    public void plusUserCount(MyRoomRequestDTO myRoomRequestDTO) {
        System.out.println("plusUserCount");
        Integer memberId = myRoomRequestDTO.getMemberId();
        Optional<Member> member = memberRepository.findById(memberId);
        Integer userCount = member.get().getUserCount();
        member.get().setUserCount(userCount+1);

        memberRepository.save(member.get());
    }

    public void addStuff(MyRoomAddStuffRequestDTO myRoomAddStuffRequestDTO) {
        System.out.println("addStuff");
        //Member 찾기
        Optional<Member> member = memberRepository.findById(myRoomAddStuffRequestDTO.getMemberId());

        //myRoomAddStuffRequestDTO 요소 가져오기
        String title = myRoomAddStuffRequestDTO.getTitle();
        String outContent = myRoomAddStuffRequestDTO.getOutContent();
        String inContent = myRoomAddStuffRequestDTO.getInContent();

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

//        // 이미지
//        PostImage postImage;
//
//        // chatRoom 생성
//        ChatRoom chatRoom;


        //post 객체 생성
        Post post = new Post(member.get(), title, outContent, inContent, 0, 0, currentDateTime, currentDateTime);
        postJPARepository.save(post);
    }
}
