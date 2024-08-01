package inandout.backend.entity.member;

import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.post.InOut;
import inandout.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 접근 권한을 Private로 하면 프록시 객체 생성에 문제가 생기고,
// 접근 권한을 Public으로 하면 무분별한 객체 생성 및 setter를 통한 값 주입을 할 수 있기에
// 접근 권한을 Protected로 작성
@DynamicInsert // 엔티티를 save할 때 null 값은 배제하고 insert 쿼리를 날리도록 한다.
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

//    @Column(name = "member_img_url", length = 500, nullable = false)
//    private String memberImgUrl;

//    @Column(name = "user_count", nullable = false)
//    private int userCount;

    @Column(length = 500, nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(name = "platform_id", length = 500)
    private String platformId;

    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = false)
    @ColumnDefault("'NONCERTIFIED'")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isPublic;

    private String authToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<ChatRoom> chatrooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<InOut> inOuts = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("1")
    private int memberImageId;

    public static Member  createSocialMember(String name, String email, String password, Platform platform, String platformId, MemberStatus memberStatus) {
        Member member = new Member();

        member.name = name;
        member.email = email;
        member.password = password;
        member.platform = platform;
        member.platformId = platformId;
        member.status = memberStatus;

        return member;
    }

    public static Member createGeneralMember(String username, String email, String password, Platform platform, Integer memberImageId) {
        Member member = new Member();

        member.name = username;
        member.email = email;
        member.password = password;
        member.platform = platform;
        member.memberImageId = memberImageId;

        return member;
    }

    public void updateToken(String token) {
        this.authToken = token;
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }
    public void updateNickname(String nickname) {
        this.name = nickname;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public String getEmail() {
        return email;
    }

}
