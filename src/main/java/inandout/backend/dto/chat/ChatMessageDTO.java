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

//    public enum MessageType{
//        WHOLE, BULLETIN
//    }
//
//    private MessageType messageType;
//    private Long senderId;
//    private String message;
//    private Long chatRoodId;

}
