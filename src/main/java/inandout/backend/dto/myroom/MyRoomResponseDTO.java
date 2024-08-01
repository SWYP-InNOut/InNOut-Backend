package inandout.backend.dto.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyRoomResponseDTO {
    private String memberName;
    private boolean isPublic;
    private List<ChatResponseDTO> chats;
    private List<MyRoomPostDTO> posts; // 최신순
//    private List<MyRoomPostDTO> postsIn; // in많은순
//    private List<MyRoomPostDTO> postsOut; // out많은순
//    private List<MyRoomPostDTO> postsOldest; // 오래된순

}
