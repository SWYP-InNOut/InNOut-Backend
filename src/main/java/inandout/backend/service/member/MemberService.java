package inandout.backend.service.member;

import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    public void updateNickname(String email, String nickname) {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.ifPresent(value -> value.updateNickname(nickname));
    }
}
