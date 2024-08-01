package inandout.backend.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class ChatResponseDTO {

    private Integer chatId;
    private Integer sender;
    private LocalDateTime createdAt;
    private String content;

    private boolean isReply; // 답장인지 여부
    private String replyContent; // 답장 채팅 내용(isReply==true 일때만 유효)
    private String replySenderName; // 답장할 사람 이름

    private boolean isPostChat; //게시물 채팅인지
    private String stuffName; // 어떤 물건에 관련된 채팅인지 (isPostChat==true 일때만 유효)

    public ChatResponseDTO(Integer chatId, Integer sender, LocalDateTime createdAt, String content, boolean isReply) {
        this.chatId = chatId;
        this.sender = sender;
        this.createdAt = createdAt;
        this.content = content;
        this.isReply = isReply;
    }

    public void updateReply(String replyContent, String replySenderName) {
        this.replyContent = replyContent;
        this.replySenderName = replySenderName;
    }

    public void updatePost(boolean isPostChat, String stuffName) {
        this.isPostChat = isPostChat;
        this.stuffName = stuffName;
    }

}
