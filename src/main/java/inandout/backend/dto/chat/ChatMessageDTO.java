package inandout.backend.dto.chat;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType messageType;
    private String roomId;
    private String sender;
    private String message;
    private boolean isReply; // 답장인지 여부
    private int chatId; // 어떤 채팅에 대한 답장인지


}
