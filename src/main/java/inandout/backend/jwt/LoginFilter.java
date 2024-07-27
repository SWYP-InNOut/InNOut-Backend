package inandout.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import inandout.backend.dto.login.CustomMemberDetails;
import inandout.backend.dto.member.LoginResponseDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.service.login.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일

    // "/login"으로 요청이 오면 해당 메서드 실행됨
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청에서 username, password 추출
        // TODO: username, password를 body에 넣게 바꿔야 됨
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        //token에 담은 username과 password 검증을 위해 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    // authentication의 principal에 authenticationManager가 검증 완료한 customMemberDetails(username과 password)가 담겨 있음
    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        //UserDetails
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        String email = customMemberDetails.getUsername();

        Optional<Member> member = memberRepository.findByEmail(email);

        TokenInfo tokenInfo = jwtUtil.generateToken(email);

        // 응답의 콘텐츠 타입을 JSON으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.addHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        response.setHeader(HttpHeaders.SET_COOKIE, "refreshToken=" + tokenInfo.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenValidTime + "; SameSite=None");

        // JSON 응답 작성
        LoginResponseDTO nicknameDTO = new LoginResponseDTO(member.get().getId(), member.get().getName());
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        writer.write(mapper.writeValueAsString(nicknameDTO));
        writer.flush();
        writer.close();

        // redis에 refreshToken, memberId 저장
        // TODO: 추후에 clientIp도 저장할 예정
        if (memberRepository.findByEmail(email).isPresent() && memberRepository.isActiveMember(email)) {
            redisService.setValues(tokenInfo.getRefreshToken(), email);

            log.info(redisService.getEmail(tokenInfo.getRefreshToken()));
        }
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }
}
