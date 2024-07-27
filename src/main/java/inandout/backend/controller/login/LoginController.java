package inandout.backend.controller.login;

import inandout.backend.argumentresolver.MemberEmail;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.jwt.TokenInfo;
import inandout.backend.service.login.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일

    @PostMapping("/regenerate-token")
    public BaseResponse<String> regenerateToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        TokenInfo tokenInfo = loginService.reissue(Objects.requireNonNull(refreshToken).getValue());

        response.addHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        response.setHeader("Set-Cookie",
                "refreshToken=" + tokenInfo.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenValidTime);

        return new BaseResponse<>("토큰 발급이 완료되었습니다.");
    }

    @PostMapping("/logout")
    public BaseResponse<String> logout(@MemberEmail String email) {
        log.info(email);
        loginService.logout(email);

        return new BaseResponse<>("로그아웃이 완료되었습니다.");
    }

}
