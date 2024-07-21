package inandout.backend.dto.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomResponseDTO {
    private String MemberName;
    private List<ChatResponseDTO> chats;
    private List<MyRoomPostDTO> posts;
}
