package inandout.backend.service.login;

import inandout.backend.common.exception.MemberException;
import inandout.backend.config.SecurityConfig;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_EMAIL;
import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_NICKNAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        boolean isExistEmail = memberRepository.existsMemberByEmail(email);
        boolean isExistName = memberRepository.existsMemberByName(username);

        if (isExistEmail) {
            log.error(DUPLICATED_EMAIL.getMessage());
            throw new MemberException(DUPLICATED_EMAIL);
        }

        if (isExistName) {
            log.error(DUPLICATED_NICKNAME.getMessage());
            throw new MemberException(DUPLICATED_NICKNAME);
        }

        Member member = Member.createGeneralMember(username, email, bCryptPasswordEncoder.encode(password), Platform.GENERAL);

        memberRepository.save(member);
    }
}
