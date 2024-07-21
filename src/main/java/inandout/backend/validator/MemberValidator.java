package inandout.backend.validator;

import inandout.backend.common.exception.MemberException;
import inandout.backend.repository.login.MemberRepository;
import lombok.extern.slf4j.Slf4j;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_EMAIL;
import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_NICKNAME;

@Slf4j
public class MemberValidator {
    public static void validateDuplicateEmail(MemberRepository memberRepository, String email) {
        boolean isExistEmail = memberRepository.existsMemberByEmail(email);
        if (isExistEmail) {
            log.error(DUPLICATED_EMAIL.getMessage());
            throw new MemberException(DUPLICATED_EMAIL);
        }
    }

    public static void validateDuplicateUsername(MemberRepository memberRepository, String username) {
        boolean isExistName = memberRepository.existsMemberByName(username);
        if (isExistName) {
            log.error(DUPLICATED_NICKNAME.getMessage());
            throw new MemberException(DUPLICATED_NICKNAME);
        }
    }

}
