package inandout.backend.jwt;

import inandout.backend.service.login.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LogoutHandler extends SecurityContextLogoutHandler {
    private final RedisService redisService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // redis의 refreshToken 삭제
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        redisService.deleteRefreshToken(Objects.requireNonNull(refreshToken).getValue());

        if (this.isInvalidateHttpSession()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }

        String refreshTokenCookie = "refreshToken=; Path=/; HttpOnly; Secure; Max-Age=0; SameSite=None";
        response.setHeader("Set-Cookie", refreshTokenCookie);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setHeader("Expires", "0"); // Proxies

        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);
    }
}
