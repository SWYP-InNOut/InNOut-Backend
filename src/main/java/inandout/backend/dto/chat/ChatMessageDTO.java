package inandout.backend.dto.chat;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatMessageDTO {

    public ChatMessageDTO(Long roomId, String message) {
    }

    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType; // 메시지 타입
    private Long RoomId; // 방 번호
    private Long senderId; // 채팅을 보낸 사람
    private String message; // 메시지

//    public enum MessageType{
//        WHOLE, BULLETIN
//    }
//
//    private MessageType messageType;
//    private Long senderId;
//    private String message;
//    private Long chatRoodId;

}
