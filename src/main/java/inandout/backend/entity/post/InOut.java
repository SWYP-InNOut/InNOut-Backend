package inandout.backend.entity.post;

import inandout.backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inout_id", nullable = false)
    private int id;

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isCheckIn;

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isCheckOut;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isMember;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 양방향 연관 관계 / 연관 관계의 주인
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 양방향 연관 관계 / 연관 관계의 주인
    @JoinColumn(name = "member_id")
    private Member member;




}
