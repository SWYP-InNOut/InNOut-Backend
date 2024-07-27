package inandout.backend.config;

import inandout.backend.jwt.JWTFilter;
import inandout.backend.jwt.JWTUtil;
import inandout.backend.jwt.LoginFilter;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.service.login.RedisService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //authenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

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

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join", "/healthcheck", "/regenerate-token",
                                "/auth/verify", "/chat", "/ws/chat", "/kakaologin/callback",
                                "/myroom/chat","/myroom/post/{postId}/chat",
                                "/others/room/detail/{postId}/chat", "/myroom", "/myroom/addstuff",
                                "/myroom/post/{postId}","/others", "/in", "/out", "/nickname", "/password", "/check-password",
                                "/others/room","/others/post/{postId}").permitAll()    // 모든 권한 허용
                        .requestMatchers("/admin").hasRole("ADMIN")    // "ADMIN"이라는 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/main").authenticated());    // 로그인 한 사용자만 접근 가능

        //LoginFilter 이전에 JWTFilter 등록
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http.addFilterAt(new LoginFilter(memberRepository, authenticationManager(authenticationConfiguration), jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // 세션을 STATELESS 상태로 설정


        http.logout(logout -> logout
                .logoutUrl("/logout")
                // 로그아웃 핸들러 추가 (세션 무효화 처리)
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                })
                // 로그아웃 성공 핸들러 추가 (리다이렉션 처리)
                .logoutSuccessHandler((request, response, authentication) ->
                        response.sendRedirect("http://stuffinout.site/login"))
                .deleteCookies("JSESSIONID", "refreshToken"));

        return http.build();
    }
}
