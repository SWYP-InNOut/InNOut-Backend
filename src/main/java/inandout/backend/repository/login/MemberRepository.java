package inandout.backend.repository.login;

import inandout.backend.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Boolean existsMemberByEmail(String email);
    Boolean existsMemberByName(String name);
}
