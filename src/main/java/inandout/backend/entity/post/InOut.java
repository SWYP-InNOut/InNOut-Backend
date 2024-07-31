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
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;


    public InOut(boolean isCheckIn, boolean isCheckOut, boolean isMember, Post post, Member member) {
        this.isCheckIn = isCheckIn;
        this.isCheckOut = isCheckOut;
        this.isMember = isMember;
        this.post = post;
        this.member = member;
    }

    public void updateIn(boolean in) {
        this.isCheckIn = in;
    }

    public void updateOut(boolean out) {
        this.isCheckOut = out;
    }
}
