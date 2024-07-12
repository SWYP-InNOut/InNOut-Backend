package inandout.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        // csrf disable
//        http.csrf((auth) -> auth.disable());
//
//        //From 로그인 방식 disable
//        http.formLogin((auth) -> auth.disable());
//
//        //http basic 인증 방식 disable
//        http.httpBasic((auth) -> auth.disable());
//
//        //경로별 인가 작업
//        http.authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/login", "/", "/join").permitAll()    // 모든 권한 허용
//                        .requestMatchers("/admin").hasRole("ADMIN")    // "ADMIN"이라는 권한을 가진 사용자만 접근 가능
//                        .anyRequest().authenticated());    // 로그인 한 사용자만 접근 가능
//
//        //세션 설정
//        http.sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // 세션을 STATELESS 상태로 설정
//
//
//        return http.build();
//    }
//}
