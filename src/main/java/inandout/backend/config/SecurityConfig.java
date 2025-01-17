package inandout.backend.config;

import inandout.backend.jwt.*;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.service.login.CustomOAuth2UserService;
import inandout.backend.service.login.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //authenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http.csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)))
                .successHandler(customSuccessHandler)
        );

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        // TODO: 공유 URL만 경로 모든 권한 허용해주기
                        // 모든 권한 허용
                        .requestMatchers("/login", "/", "/join", "/healthcheck", "/regenerate-token", "/find-password",
                                "/auth/verify",  "/kakaologin/callback", "/kakaologin", "/kakaologin/local",
                                "/oauth2/authorization/google", "/login/oauth2/code/google", "/myroom",  "/myroom/post").permitAll()

//                        .requestMatchers( "/myroom", "/myroom/post/{postId}").anonymous()

                        // "ADMIN"이라는 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 로그인 한 사용자만 접근 가능, 즉 Header에 Authorization이 있는 경로만 허용
                        .requestMatchers("/main", "/password", "/check-password", "/nickname", "/logout",
                                "/others", "/others/room", "/others/room/detail/{postId}/chat", "/others/post/{postId}",
                                "/myroom/chat","/myroom/post/{postId}/chat", "/myroom/addstuff", "/myroom/updatestuff",
                                "/myroom/link", "/link", "/myroom/post/{postId}",
                                "/ispublic", "/inout", "/user/modify",
                                "/ws/chat", "/chat").authenticated());


        //LoginFilter 이전에 JWTFilter 등록
        http.addFilterBefore(new JWTFilter(jwtUtil, memberRepository), LoginFilter.class);

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http.addFilterAt(new LoginFilter(memberRepository, authenticationManager(authenticationConfiguration), jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // 세션을 STATELESS 상태로 설정

        http.logout(logout -> logout
                .logoutUrl("/logout")
                // 로그아웃 핸들러 추가 (세션 무효화 처리)
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/text");
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().write("로그아웃 성공");
                    }
                })
                .addLogoutHandler(new LogoutHandler(redisService)));

        return http.build();
    }
}
