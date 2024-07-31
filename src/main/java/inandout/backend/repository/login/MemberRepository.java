package inandout.backend.repository.login;

import inandout.backend.common.exception.BaseException;
import inandout.backend.entity.auth.Platform;

import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_MEMBER;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email=:email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return members.stream().findAny();
    }

    public Optional<Member> findById(Integer memberId) {
        List<Member> member = em.createQuery("select m from Member m where m.id=:memberId", Member.class)
                .setParameter("memberId", memberId).getResultList();
               // .getSingleResult();

        //return Optional.ofNullable(member);
        if (member.size() == 0) {
            throw new BaseException(NOT_FOUND_MEMBER);
        }

        return member.stream().findAny();
    }

    public Optional<Member> findByAuthToken(String authToken) {
        List<Member> members = em.createQuery("select m from Member m where m.authToken=:authToken", Member.class)
                .setParameter("authToken", authToken)
                .getResultList();

        return members.stream().findAny();
    }

    public Optional<Member> existsMemberByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return members.stream().findAny();
    }

    public boolean existsMemberByName(String name) {
        return em.createQuery("select count(m) > 0 from Member m where m.name = :name", boolean.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public boolean isNoncertifiedMember(String email) {
        return em.createQuery("select count(m)>0 from Member m where m.email=:email and m.status=:status", Boolean.class)
                .setParameter("email", email)
                .setParameter("status", MemberStatus.NONCERTIFIED)
                .getSingleResult();
    }

    public boolean isActiveMember(String email) {
        return em.createQuery("select count(m)>0 from Member m where m.email=:email and m.status=:status", Boolean.class)
                .setParameter("email", email)
                .setParameter("status", MemberStatus.ACTIVE)
                .getSingleResult();
    }

    @Transactional
    public void updateUserCount(Integer memberId, Integer userCount) {
        em.createQuery("UPDATE Member m SET m.userCount = :user_count WHERE m.id = :member_id")
                .setParameter("user_count", userCount).setParameter("member_id", memberId).executeUpdate();
    }

    public List<Member> getMembersOrderByUserCount() {
        List<Member> members = em.createQuery("SELECT m FROM Member m ORDER BY m.userCount DESC").getResultList();
        return members;
    }

    public Optional<Member> findGeneralMemberByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email=:email and m.platform=:platform", Member.class)
                .setParameter("email", email)
                .setParameter("platform", Platform.GENERAL)
                .getResultList();

        return members.stream().findAny();
    }

    public List<Integer> getMemberIsPublic() {
        List<Integer> members = em.createQuery("SELECT m.id FROM Member m WHERE m.isPublic = true").getResultList();
        return members;
    }

    public Integer getMemberImageId(Integer memberId) {
        Integer memberImageId = (Integer) em.createQuery("SELECT m.memberImageId FROM Member m WHERE m.id = :memberId")
                .setParameter("memberId", memberId).getSingleResult();

        return memberImageId;
    }
}
