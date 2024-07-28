package inandout.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import inandout.backend.common.exception.MemberException;
import inandout.backend.common.response.BaseErrorResponse;
import inandout.backend.dto.login.CustomMemberDetails;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.EXPIRED_ACCESSTOKEN;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/regenerate-token")
                || request.getRequestURI().equals("/join") || request.getRequestURI().equals("/auth/verify")
                || request.getRequestURI().equals("/kakaologin/callback")
                || request.getRequestURI().equals("/find-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        if (request.getRequestURI().equals("/myroom") || request.getRequestURI().equals("/others/room")
                || request.getRequestURI().equals("/in") || request.getRequestURI().equals("out")) {
            if (authorization == null) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            response.setStatus(401);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }


        System.out.println("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            System.out.println("=============================token expired==================================");
            // 예외 정보를 JSON 형식으로 변환
            BaseErrorResponse errorResponse = new BaseErrorResponse(EXPIRED_ACCESSTOKEN);

            // 응답의 콘텐츠 타입을 JSON으로 설정
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setCharacterEncoding("utf-8");

            // JSON 응답 작성
            PrintWriter writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(errorResponse));
            writer.flush();
            writer.close();
            return;
        }

        //토큰에서 email 획득
        String email = jwtUtil.getEmail(token);

        //userEntity를 생성하여 값 set
        Member member = Member.createGeneralMember("tempname", email, "temppassword", Platform.GENERAL);

        //UserDetails에 회원 정보 객체 담기
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
