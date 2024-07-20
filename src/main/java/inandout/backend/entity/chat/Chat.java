package inandout.backend.entity.chat;

import inandout.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 양방향 연관 관계 / 연관 관계의 주인
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private int sender;

    @Column(name = "chat_content", nullable = false)
    private String chatContent;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Column(name = "is_reply")
    private boolean isReply;

    @Column(name = "reply_chat_id")
    private Integer replyChatId;

    @Column(name = "reply_member_id")
    private Integer replyMemberId;

}
