package inandout.backend.service.chat;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.entity.chat.Chat;
import inandout.backend.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    // 전체 채팅 조회
    public List<ChatResponseDTO> getTotalChat(Integer memberId) {

        //메인 채팅 + 모든 게시물 채팅 roomid
        List<Integer> wholeChatRoomIdList =chatRepository.getTotalChatIdList(memberId);

        List<ChatResponseDTO> chatResponseDTOList = chatRepository.getChats(wholeChatRoomIdList);

        return chatResponseDTOList;
    }

    // 게시물 채팅 조회
    public List<ChatResponseDTO> getPostChat(Integer memberId, Integer postId) {

        //메인채팅 + 특정 게시물 채팅 roomids
        List<Integer> ChatRoomIdList = new ArrayList<>();
        ChatRoomIdList.add(chatRepository.getMainChatRoomIdByMemberId(memberId));
        ChatRoomIdList.add(chatRepository.getPostChatRoomIdByPostId(postId));

        List<ChatResponseDTO> chatResponseDTOList = chatRepository.getChats(ChatRoomIdList);

        return chatResponseDTOList;
    }

    // 타 사용자 게시물 채팅 조회
    public List<ChatResponseDTO> getOtherPostChat(Integer postId) {
        System.out.println("getOtherPostChat");

        List<Integer> ChatRoomIdList = new ArrayList<>();

        Integer memberId = chatRepository.getMemberIdByPostId(postId);
        System.out.println("memberId: "+memberId);
        ChatRoomIdList.add(chatRepository.getMainChatRoomIdByMemberId(memberId));
        ChatRoomIdList.add(chatRepository.getPostChatRoomIdByPostId(postId));

        List<ChatResponseDTO> chatResponseDTOList = chatRepository.getChats(ChatRoomIdList);

        return chatResponseDTOList;
    }

}
