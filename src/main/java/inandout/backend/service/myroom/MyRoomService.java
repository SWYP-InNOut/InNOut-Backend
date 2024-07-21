package inandout.backend.service.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.dto.myroom.MyRoomRequestDTO;
import inandout.backend.dto.myroom.MyRoomResponseDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.myroom.MyRoomRepository;
import inandout.backend.repository.post.PostRepository;
import inandout.backend.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public MyRoomResponseDTO getMyRoomInfo(MyRoomRequestDTO myRoomRequestDTO) {
        Integer memberId = myRoomRequestDTO.getMemberId();
        //memberId로 memberName 얻기
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        String memberName = member.get().getName();

        // getTotalChat 로 채팅 가져오기 (일단 상위 5개만 우선적으로)
        List<ChatResponseDTO> chatResponseDTOList = chatService.getTotalChat(memberId);
        List<ChatResponseDTO> chatResponseDTOS;
        if(chatResponseDTOList.size() > 5){
            chatResponseDTOS = chatResponseDTOList.subList(0, 5);
        }else{
            chatResponseDTOS = chatResponseDTOList;
        }

        MyRoomResponseDTO myRoomResponseDTO = new MyRoomResponseDTO();
        // 게시물 id 가져오기
        List<Integer> postIdList = postRepository.getPostIdsByMemberId(memberId);

        // 게시물 id로 정보 가져와서 DTO에 추가

        return myRoomResponseDTO;
    }



}
