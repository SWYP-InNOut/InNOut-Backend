package inandout.backend.entity.member;

import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.chat.ChatRoom;
import inandout.backend.entity.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
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

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

//    @Column(name = "member_img_url", length = 500, nullable = false)
//    private String memberImgUrl;

    @Column(name = "user_count", nullable = false)
    private int userCount;

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
    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isPublic;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)// 다대다(다대일, 일대다) 단방향 연관 관계 / 연관 관계 주인의 반대편
    private List<ChatRoom> chatrooms = new ArrayList<>();
    public static Member  createSocialMember(String name, String email, String password, Platform platform, String platformId) {
        Member member = new Member();

        member.name = name;
        member.email = email;
        member.password = password;
        member.platform = platform;
        member.platformId = platformId;

        return member;
    }

    public static Member createGeneralMember(String username, String email, String password, Platform platform) {
        Member member = new Member();

        member.name = username;
        member.email = email;
        member.password = password;
        member.platform = platform;

        return member;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public String getEmail() {
        return email;
    }

}
