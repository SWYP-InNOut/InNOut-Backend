package inandout.backend.entity.post;

import inandout.backend.entity.chat.Chat;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 양방향 연관 관계 / 연관 관계의 주인
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(name = "out_content", length = 500, nullable = false)
    private String outContent;

    @Column(name = "in_content", length = 500, nullable = false)
    private String inContent;

    @Column(name = "out_count", nullable = false)
    private int outCount;

    @Column(name = "in_count", nullable = false)
    private int inCount;

//    @Column(name = "user_count", nullable = false)
//    private int userCount;

    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<Chat> chats = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<InOut> inOuts = new ArrayList<>();

    @Column(name = "user_count", nullable = false)
    @ColumnDefault("0")
    private int userCount;

    public Post(Member member, String title, String outContent, String inContent, int outCount, int inCount, LocalDateTime createdAt, LocalDateTime updatedAt, ChatRoom chatRoom) {
        this.member = member;
        this.title = title;
        this.outContent = outContent;
        this.inContent = inContent;
        this.outCount = outCount;
        this.inCount = inCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
//        this.postImages = postImages;
//        this.chats = chats;
        this.chatRoom = chatRoom;
    }

    public void updateOutCount(int outCount) {
        this.outCount = outCount;
    }

    public void updateInCount(int inCount) {
        this.inCount = inCount;
    }
}
