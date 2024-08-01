package inandout.backend.service.login;

import inandout.backend.Util.EmailUtils;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailUtils emailUtils;
    private final MemberValidator memberValidator;

    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();
        Integer memberImageId = joinDTO.getMemberImageId();

        // 이메일로 가입된 회원이 있는지 검증
        memberValidator.validateDuplicateEmail(email);

        String authToken = UUID.randomUUID().toString();
        if (!memberValidator.validateDuplicateEmailAndCheckExpiredToken(email)) {
            // 이메일 중복이 아닐 때만 이름 중복 검사 실시
            memberValidator.validateDuplicateUsername(username);
        } else {
            if (memberRepository.isNoncertifiedMember(email)) {
                // 이메일 중복 + NONCERTIFIED 회원 + 토큰 만료 인 경우
                Optional<Member> member = memberRepository.existsMemberByEmail(email);
                member.get().updateToken(authToken);
                emailUtils.sendEmail(member.get());
                return;
            }
        }

        Member member = Member.createGeneralMember(username, email, bCryptPasswordEncoder.encode(password), Platform.GENERAL, memberImageId);
        member.updateToken(authToken);

        memberRepository.save(member);

        emailUtils.sendEmail(member);
    }

    public boolean updateByVerifyToken(String token) {
        Optional<Member> member = memberRepository.findByAuthToken(token);

        if (member.isPresent() && !memberValidator.isExpired(member.get())) {
            member.get().updateStatus(MemberStatus.ACTIVE);  // 변경감지
            return true;
        } else {
            return false;
        }
    }
}
