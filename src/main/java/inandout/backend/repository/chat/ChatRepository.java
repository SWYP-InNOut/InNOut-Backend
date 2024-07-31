package inandout.backend.repository.chat;

import com.sun.tools.javac.Main;
import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.entity.chat.Chat;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.post.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//public interface ChatRepository extends JpaRepository<Chat, String> {
//    Optional<Chat> findByChatRoom(ChatRoom chatRoom);
//
//    @Query("select c FROM Chat c WHERE c.chatRoom IN (:chatroom_ids)")
//    List<Object[]> getChats(@Param("chatroom_ids") List<String> ChatRoomIds);
//
//
//}

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRepository {

    private final EntityManager em;

    @Autowired
    public PostRepository postRepository;


    //채팅룸 id 리스트 받아서 채팅 최신순으로 반환
    public List<ChatResponseDTO> getChats(List<Integer> chatRoomIds){
        System.out.println(chatRoomIds.size());
        System.out.println("ChatRepository/getChats");
        // 최근꺼부터 반환하는걸로 함
        List<Chat> resultList = em.createQuery("SELECT c FROM chat c WHERE c.chatRoom.id IN (:chatroom_ids) ORDER BY c.sendAt DESC")
                .setParameter("chatroom_ids", chatRoomIds)
                .getResultList();
        System.out.println("여기까지옴");
        System.out.println(resultList.size());

        List<ChatResponseDTO> chatResponseDTOList = new ArrayList<>();

        for(Chat result : resultList){
            ChatResponseDTO chatResponseDTO = new ChatResponseDTO();

            chatResponseDTO.setChatId((long) result.getId());
            chatResponseDTO.setContent(result.getChatContent());
            chatResponseDTO.setSender((long) result.getSender());
            chatResponseDTO.setCreatedAt(result.getCreatedAt());

            //답장 관련
            chatResponseDTO.setReply(result.isReply());
            if(result.isReply()){ // 답장있으면
                String replyContent = getChatContentByChatId(result.getReplyChatId());
                chatResponseDTO.setReplyContent(replyContent);

                String replyName = getMemberNameByMemberId(result.getReplyMemberId());
                chatResponseDTO.setReplySenderName(replyName);
            }

            //게시물 관련
            if(result.getPost() != null){
                chatResponseDTO.setPostChat(true);
                chatResponseDTO.setStuffName(postRepository.getStuffNameByPostId(result.getPost().getId()));
            }

            chatResponseDTOList.add(chatResponseDTO);
        }

        return chatResponseDTOList;
    }

    // 전체채팅룸Id + 모든 게시물 채팅룸Id  (memberId)
    public List<Integer> getTotalChatIdList(Integer memberId) {
       List<Integer> resultList = em.createQuery("SELECT cr.id FROM ChatRoom cr WHERE cr.member.id = :member_id")
               .setParameter("member_id", memberId).getResultList();
        return resultList;
    }

    public Integer getMainChatRoomIdByMemberId(Integer memberId) {

        List<Integer> MainChatRoomIds = em.createQuery("SELECT rm.id FROM ChatRoom rm WHERE rm.member.id = :member_id AND rm.id NOT IN (" +
                        "SELECT p.chatRoom.id FROM Post p WHERE p.member.id = :member_id)")
                .setParameter("member_id", memberId).getResultList();
        if(MainChatRoomIds.size() < 1){
            System.out.println("채팅룸이 없어요");
            return 0;
        }
        return MainChatRoomIds.get(0);
    }

    public Integer getPostChatRoomIdByPostId(Integer postId) {

        Integer PostChatRoomId = (Integer) em.createQuery("SELECT p.chatRoom.id FROM Post p WHERE p.id = :post_id")
                .setParameter("post_id", postId).getSingleResult();

        System.out.println(PostChatRoomId);

        return PostChatRoomId;
    }

    public Integer getMemberIdByPostId(Integer postId) {

        Integer MemberId = (Integer) em.createQuery("SELECT p.member.id FROM Post p WHERE p.id = :post_id")
                .setParameter("post_id", postId).getSingleResult();

        return MemberId;
    }

    public String getChatContentByChatId(Integer chatId) {
        String content = (String) em.createQuery("SELECT c.chatContent FROM chat c WHERE c.id = :chat_id")
                .setParameter("chat_id", chatId).getSingleResult();
        if (content.isEmpty()) {
            System.out.println("null임");
            return null;
        }
        return content;
    }

    public String getMemberNameByMemberId(Integer memberId) {
        String memberName = (String) em.createQuery("SELECT m.name FROM Member m WHERE m.id = :member_id")
                .setParameter("member_id", memberId).getSingleResult();
        return memberName;
    }

    @Transactional
    public void save(Chat chat) {
        em.persist(chat);
    }

//    public String getStuffNameByPostId(Integer postId) {
//        String stuffName = (String) em.createQuery("SELECT p.title FROM Post p WHERE p.id = : post_id")
//                .setParameter("post_id", postId).getSingleResult();
//        return stuffName;
//    }



}
