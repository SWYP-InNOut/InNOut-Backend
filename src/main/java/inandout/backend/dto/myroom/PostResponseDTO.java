package inandout.backend.dto.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private String memberName;
    private String title;
    private String inContent;
    private String outContent;
    private LocalDateTime createdAt;
    private List<ChatResponseDTO> chats;
    private List<String> imageUrls;


}
