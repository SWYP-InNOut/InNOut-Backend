package inandout.backend.service.login;

import inandout.backend.config.SecurityConfig;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        boolean isExist = memberRepository.existsByName(username);

        if (isExist) {
            return;
        }

        Member member = Member.createMember(username, email, bCryptPasswordEncoder.encode(password), Platform.GENERAL);

        memberRepository.save(member);
    }
}
