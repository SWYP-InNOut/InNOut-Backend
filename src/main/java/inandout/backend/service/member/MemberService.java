package inandout.backend.service.member;

import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final PasswordEncoder passwordEncoder;

    public void updateNickname(String email, String nickname) {
        memberValidator.validateDuplicateUsername(nickname);

        Optional<Member> member = memberRepository.findByEmail(email);
        member.ifPresent(value -> value.updateNickname(nickname));
    }

    public void validatePassword(String email, String password) {
        memberValidator.validatePassword(email, password);
    }

    public void updatePassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.ifPresent(value -> value.updatePassword(passwordEncoder.encode(password)));
    }
}
