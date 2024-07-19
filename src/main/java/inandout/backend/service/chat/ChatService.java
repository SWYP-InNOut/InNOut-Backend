package inandout.backend.service.chat;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.entity.chat.Chat;
import inandout.backend.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public List<ChatResponseDTO> getTotalChat(Long memberId) {

        //메인 채팅 + 모든 게시물 채팅 roomid
        List<Long> wholeChatRoomIdList = new ArrayList<>();
        wholeChatRoomIdList.add(1L);
        wholeChatRoomIdList.add(2L);
        wholeChatRoomIdList.add(3L);

        List<ChatResponseDTO> chatResponseDTOList = chatRepository.getChats(wholeChatRoomIdList);

        //메인채팅 + 특정 게시물 채팅

        return chatResponseDTOList;
    }

    public List<ChatResponseDTO> getBulletinChat(Long memberId) {

        ////메인채팅 + 특정 게시물 채팅 roomid
        List<Long> BulletinChatRoomIdList = new ArrayList<>();

        BulletinChatRoomIdList.add(2L);
        BulletinChatRoomIdList.add(3L);

        List<ChatResponseDTO> chatResponseDTOList = chatRepository.getChats(BulletinChatRoomIdList);



        return chatResponseDTOList;
    }

}
