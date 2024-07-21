package inandout.backend.chat.stomp;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StompChatRoomRepository {
    private Map<String, StompChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<StompChatRoomDTO> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<StompChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public StompChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public StompChatRoomDTO createChatRoomDTO(String name){
        StompChatRoomDTO room = StompChatRoomDTO.create(name);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}
