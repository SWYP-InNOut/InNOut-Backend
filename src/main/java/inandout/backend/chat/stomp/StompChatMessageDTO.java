package inandout.backend.chat.stomp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StompChatMessageDTO {
    private String roomId;
    private String writer;
    private String message;
}
