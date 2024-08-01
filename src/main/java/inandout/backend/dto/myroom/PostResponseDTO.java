package inandout.backend.dto.myroom;

import inandout.backend.dto.chat.ChatResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponseDTO {

    private String ownerName;
    private Integer ownerId;
    private String title;
    private String inContent;
    private String outContent;
    private LocalDateTime createdAt;
    private boolean isCheckedIn;
    private boolean isCheckedOut;
    private List<ChatResponseDTO> chats;
    private List<String> imageUrls;

}
