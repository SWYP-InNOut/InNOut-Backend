package inandout.backend.controller.login;


import com.fasterxml.jackson.databind.ObjectMapper;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.KakoLoginResponseDTO;
import inandout.backend.dto.login.LoginDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.jwt.JWTUtil;
import inandout.backend.jwt.TokenInfo;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.service.login.KakaoLoginService;
import inandout.backend.service.login.RedisService;
import inandout.backend.service.login.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@RequiredArgsConstructor
@RestController
@RequestMapping("/kakaologin")
public class KakaoLoginController {
    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일
    private final JWTUtil jwtUtil;
    @Autowired
    public KakaoLoginService kakaoLoginService;
    @Autowired
    public RedisService redisService;
    @Autowired
    public UserService userService;
    @Autowired
    public MemberRepository memberRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/callback")
    public void KakaoLogin(@RequestParam(value = "code") String code) {
        System.out.println("KakaoLoginController/KakaoLogin -> TEST");
        System.out.println("Test로 들어온 code: "+code);


    }


    @GetMapping("")
    public ResponseEntity<KakoLoginResponseDTO> KakaoLoginCallBack(@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        System.out.println("KakaoLoginController/KakaoLoginCallBack");
        KakoLoginResponseDTO kakoLoginResponseDTO = null;


// 주석 풀어야돼

        // 엑세스/리프레쉬 토큰 받기
        HashMap<String, String> kakaoToken  = kakaoLoginService.getAccessToken(code);

        // 유저 정보 받기
        HashMap<String, Object> kakaoUserInfo = kakaoLoginService.getUserInfo(kakaoToken.get("accessToken"));



        //카카오&일반로그인 accessToken 방식이 달라 문제 -> 카카오 Token 사용X
        //String accessToken = kakaoToken.get("accessToken");
        //String refreshToken = kakaoToken.get("refreshToken");

        String email = (String) kakaoUserInfo.get("email");
        boolean isMember;

        TokenInfo tokenInfo= jwtUtil.generateToken(email);
        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        //redis에 refreshToken 저장
        redisService.setValues(email, refreshToken);

        // email로 회원 찾기
        Optional<Member> member = userService.findUser(email);

        //String newRefreshToken;
        // 로그인할때마다 refreshToken 새로 생성

        if (member.isPresent()) {   //회원 -> 로그인처리
            System.out.println("회원임");
            isMember = true;


        }else{  //비회원 ->가입
            System.out.println("비회원임");

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setName("홍길동");  // 닉네임 랜덤으로 부여
            loginDTO.setEmail(email);
            loginDTO.setPassword("");
            loginDTO.setPlatform(Platform.KAKAO);
            loginDTO.setPlatformId("1");
            loginDTO.setStatus(MemberStatus.INACTIVE);

            System.out.println("저장!");
            userService.save(loginDTO);
            isMember = false;

        }


        Member loginMember = memberRepository.findByEmail(email).get();
        System.out.println("loginMember; "+loginMember.getId());
        kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken,isMember, loginMember.getId());

        response.addHeader("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        response.setHeader("Set-Cookie","refreshToken=" + tokenInfo.getRefreshToken() + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenValidTime);


        return ResponseEntity.ok(kakoLoginResponseDTO);

//        //accessToken 만료되었는지 검사
//        boolean isTokenValid = kakaoLoginService.isValidToken("KMXxzLPp_GjjTaMW1-3Z8t2GmCRxTqV9AAAAAQopyV8AAAGQplhQWxKZRqbpl2cW");
//        System.out.println("accessToken 유효한지: "+isTokenValid);

       // return ResponseEntity.ok().body(kakoLoginResponseDTO);
    }
}
