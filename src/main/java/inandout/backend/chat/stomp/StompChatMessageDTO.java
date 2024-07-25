package inandout.backend.chat.stomp;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StompChatMessageDTO {
//    private String roomId;
//    private String writer;
//    private String message;

    private Integer chatRoomId;
    private Boolean isReply;
  //  private  Integer postId;
    private Integer replyChatId;
    private Integer replyMemberId;
    private Integer sender;
   // private LocalDateTime createdAt;
    private String chatContent;
    private Boolean isFromMainChat;

}
