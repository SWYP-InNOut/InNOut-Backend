package inandout.backend.repository.chat;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.entity.chat.Chat;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Repository
public class ChatRepository {

    private final EntityManager em;


    public List<ChatResponseDTO> getChats(List<Long> chatRoomIds){
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

            chatResponseDTOList.add(chatResponseDTO);
        }

        return chatResponseDTOList;
    }

}
