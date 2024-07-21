package inandout.backend.repository.login;

import inandout.backend.entity.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//public interface MemberRepository extends JpaRepository<Member, Long> {
//    Optional<Member> findByEmail(String email);
//    Boolean existsMemberByEmail(String email);
//    Boolean existsMemberByName(String name);
//    Optional<Member> findByAuthToken(String authToken);
//
//    @Query("update Member m set m.status='NONCERTIFIED' where m.authToken=:authToken")
//    void updateStateByToken(@Param("authToken") String authToken);
//}

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findByEmail(String email) {
        Member member = em.createQuery("select m from Member m where m.email=:email", Member.class)
                .setParameter("email", email)
                .getSingleResult();

        return Optional.ofNullable(member);
    }

    public Optional<Member> findById(Integer memberId) {
        Member member = em.createQuery("select m from Member m where m.id=:memberId", Member.class)
                .setParameter("memberId", memberId)
                .getSingleResult();

        return Optional.ofNullable(member);
    }

    public Optional<Member> findByAuthToken(String authToken) {
        Member member = em.createQuery("select m from Member m where m.authToken=:authToken", Member.class)
                .setParameter("authToken", authToken)
                .getSingleResult();

        return Optional.ofNullable(member);
    }

    public boolean existsMemberByEmail(String email) {
        return  em.createQuery("select count(m) > 0 from Member m where m.email = :email", boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public boolean existsMemberByName(String name) {
        return em.createQuery("select count(m) > 0 from Member m where m.name = :name", boolean.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public void updateStateByToken(String authToken) {
        em.createQuery("update Member m set m.status='ACTIVE' where m.authToken=:authToken")
                .setParameter("authToken", authToken)
                .executeUpdate();
    }

}
