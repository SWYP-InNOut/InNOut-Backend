package inandout.backend.service.chat;

import inandout.backend.chat.ChatRoomRepository;
import inandout.backend.chat.stomp.StompChatMessageDTO;
import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.entity.chat.Chat;
import inandout.backend.entity.post.Post;
import inandout.backend.repository.chat.ChatRepository;
import inandout.backend.repository.chat.ChatRoomJPARepository;
import inandout.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChatRoomJPARepository chatRoomJPARepository;

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


    // 채팅 저장
    public void saveChat(StompChatMessageDTO stompChatMessageDTO) throws Exception {
        System.out.println("saceChat");

        Integer chatRoomId = stompChatMessageDTO.getChatRoomId();

        Boolean isReply = stompChatMessageDTO.getIsReply();
        Boolean isFromMainChat = stompChatMessageDTO.getIsFromMainChat();

        System.out.println("isReplt:?: "+isReply);
        System.out.println("메인에서옴? "+isFromMainChat);
        //채팅방id로 post찾기
        Post post;
        if (isFromMainChat) {
            post = null;
        } else {
            post = postRepository.getPostByRoodId(chatRoomId);
        }


        //시간
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // Chat생성
        Chat chat = new Chat(post, stompChatMessageDTO.getSender(), stompChatMessageDTO.getChatContent(), dateTime, dateTime, dateTime,
                chatRoomJPARepository.findById(chatRoomId).get(), isReply, stompChatMessageDTO.getReplyChatId(), stompChatMessageDTO.getReplyMemberId(), stompChatMessageDTO.getIsFromMainChat());

        chatRepository.save(chat);
    }


}
