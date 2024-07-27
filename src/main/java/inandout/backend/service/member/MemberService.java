package inandout.backend.service.member;

import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    public void updateNickname(String email, String nickname) {
        memberValidator.validateDuplicateUsername(nickname);
        System.out.println("email: "+email);
        Optional<Member> member = memberRepository.findByEmail(email);
        System.out.println(member.get().getId());

        member.ifPresent(value -> value.updateNickname(nickname));
        if (member.isPresent()) {
            System.out.println("member존재함!");
            member.get().updateStatus(MemberStatus.ACTIVE);
            System.out.println(member.get().getStatus());
        }

    }
}
