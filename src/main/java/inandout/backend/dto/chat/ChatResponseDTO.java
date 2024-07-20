package inandout.backend.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {

    private Long chatId;
    private Long sender;
    private LocalDateTime createdAt;
    private String content;

    private boolean isReply; // 답장인지 여부
    private String replyContent; // 답장 채팅 내용(isReply==true 일때만 유효)
    private String replySenderName; // 답장할 사람 이름

    private boolean isPostChat; //게시물 채팅인지
    private String stuffName; // 어떤 물건에 관련된 채팅인지 (isPostChat==true 일때만 유효)



}
