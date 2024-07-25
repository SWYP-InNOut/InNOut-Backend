package inandout.backend.repository.chat;

import inandout.backend.entity.chat.Chat;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomJPARepository extends JpaRepository<ChatRoom, Integer> {

    Optional<ChatRoom> findById(Integer chatRoomId);

}
