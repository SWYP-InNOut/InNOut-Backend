package inandout.backend.validator;

import inandout.backend.common.exception.MemberException;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class MemberValidator {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member validateMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isEmpty()) {
            log.error(NOT_FOUND_MEMBER.getMessage());
            throw new MemberException(NOT_FOUND_MEMBER);
        }
        return member.get();
    }

    public void validateInactiveMember(String email) {
        if (!memberRepository.isActiveMember(email)) {
            log.error(INACTIVE_MEMBER.getMessage());
            throw new MemberException(INACTIVE_MEMBER);
        }
    }

    public void validateActiveMember(String email) {
        if (memberRepository.isActiveMember(email)) {
            log.error(ACTIVE_MEMBER.getMessage());
            throw new MemberException(ACTIVE_MEMBER);
        }
    }

    public boolean validateDuplicateEmailAndCheckExpiredToken(String email) {
        Optional<Member> member = memberRepository.existsMemberByEmail(email);

        if (member.isPresent()) {
            if (!isExpired(member.get())) {
                log.error(DUPLICATED_EMAIL.getMessage());
                throw new MemberException(DUPLICATED_EMAIL);  // 이메일 중복 + 토큰 만료x
            }
            return true;   // 이메일 중복 + 토큰 만료
        }
        return false;      // 이메일 중복x
    }

    public void validateDuplicateUsername(String username) {
        boolean isExistName = memberRepository.existsMemberByName(username);
        if (isExistName) {
            log.error(DUPLICATED_NICKNAME.getMessage());
            throw new MemberException(DUPLICATED_NICKNAME);
        }
    }

    public boolean isExpired(Member member) {
        LocalDateTime joinRequestTime = LocalDateTime.now();
        LocalDateTime generatedTime = member.getUpdatedAt();
//        LocalDateTime expirationDateTime = generatedTime.plusDays(1);
        LocalDateTime expirationDateTime = generatedTime.plusSeconds(60); // 일단은 유효기한 1분으로 해놓음 테스트 위해

        log.info("expiration date time: {}", expirationDateTime);
        log.info("requested time: {}", joinRequestTime);

        if (joinRequestTime.isAfter(expirationDateTime)) {
            return true;
        }

        return false;
    }

    public void validatePassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            if (!passwordEncoder.matches(password, member.get().getPassword())) {
                log.error(INVALID_PASSWORD.getMessage());
                throw new MemberException(INVALID_PASSWORD);
            }
        } else {
            log.error(NOT_FOUND_MEMBER.getMessage());
            throw new MemberException(NOT_FOUND_MEMBER);
        }
    }
}
