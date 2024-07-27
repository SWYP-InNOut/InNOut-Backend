package inandout.backend.service.login;

import ch.qos.logback.core.testUtil.RandomUtil;
import inandout.backend.Util.EmailUtils;
import inandout.backend.common.exception.MemberException;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.jwt.JWTUtil;
import inandout.backend.jwt.TokenInfo;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.security.RandomSecretKeyBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final MemberValidator memberValidator;
    private final EmailUtils emailUtils;
    private final PasswordEncoder passwordEncoder;
    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일

    public TokenInfo reissue(String refreshToken) {
        // Refresh Token Rotation 기법 사용
        // 유효한 Refresh Token인지 확인
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            redisService.deleteRefreshToken(refreshToken);
            throw new MemberException(EXPIRED_REFRESHTOKEN);
        }

        // 유효할 경우 Refresh Token 을 redis로부터 찾아서 email를 얻음
        String email = redisService.getEmail(refreshToken);
        redisService.deleteRefreshToken(refreshToken);

        // Refresh Token이 Redis에 존재하는지 검증
        if (email == null) {
            throw new MemberException(NOT_FOUND_REFRESHTOKEN);
        }

        // 회원인지 검증
        memberValidator.validateMember(email);

        // 활성화 회원인지 검증
        memberValidator.validateInactiveMember(email);

        // redis에 duration 저장하여 만료시간 설정
        // email찾고 accessToken, refreshToken 생성
        TokenInfo tokenInfo = jwtUtil.generateToken(email);
        redisService.setValues(tokenInfo.getRefreshToken(), email, Duration.ofMillis(refreshTokenValidTime));

        log.info(redisService.getEmail(tokenInfo.getRefreshToken()));

        return tokenInfo;
    }

    public void findPassword(String email) {
        Member member = memberValidator.validateGeneralMember(email);
        memberValidator.validateInactiveMember(email);

        // 임시 비밀번호 만들기
        String newPwd = RandomStringUtils.randomAlphanumeric(10);
        member.updatePassword(passwordEncoder.encode(newPwd));

        emailUtils.sendPasswordEmail(email, newPwd);
    }
}
