package inandout.backend.jwt;

import inandout.backend.dto.login.oauth2.CustomOauth2User;
import inandout.backend.repository.login.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일

    @Value("${google.callback-uri}")
    private String callbackUri;
    // https://stuffinout.site/oauth-callback?token=&memberId=

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User
        CustomOauth2User customUserDetails = (CustomOauth2User) authentication.getPrincipal();
        TokenInfo tokenInfo = jwtUtil.generateToken(customUserDetails.getMemberId());

        response.addHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        response.setHeader(HttpHeaders.SET_COOKIE, "refreshToken=" + tokenInfo.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenValidTime + "; SameSite=None");

        response.sendRedirect(callbackUri + tokenInfo.getAccessToken() +
                "&memberId=" + customUserDetails.getMemberId() +
                "&isActive=" + customUserDetails.getIsActive() +
                "&imageId=" + customUserDetails.getImageId());
    }
}
