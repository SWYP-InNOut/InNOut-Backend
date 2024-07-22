package inandout.backend.repository.chat;

import inandout.backend.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJPARepository extends JpaRepository<ChatRoom, Integer> {
}
