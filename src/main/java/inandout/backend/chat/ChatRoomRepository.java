package inandout.backend.chat;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final Map<String, ChatRoom> chatRooms;

    public void save(String roomId, ChatRoom chatRoom) {
        chatRooms.put(roomId, chatRoom);
    }

    public ChatRoom findById(Integer roomId) {
        return chatRooms.get(roomId);
    }

    public List<ChatRoom> findAll() {
        return new ArrayList<>(chatRooms.values());
    }



}
