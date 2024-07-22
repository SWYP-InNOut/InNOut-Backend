package inandout.backend.repository.login;

import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        Member member = em.createQuery("select m from Member m where m.id=:memberId", Member.class)
                .setParameter("memberId", memberId)
                .getSingleResult();

        return Optional.ofNullable(member);
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
}
