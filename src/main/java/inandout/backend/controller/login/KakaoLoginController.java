package inandout.backend.controller.login;


import inandout.backend.dto.login.KakoLoginResponseDTO;
import inandout.backend.dto.login.LoginDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.service.login.KakaoLoginService;
import inandout.backend.service.login.RedisService;
import inandout.backend.service.login.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@RestController
@RequestMapping("/kakaologin")
public class KakaoLoginController {

    @Autowired
    public KakaoLoginService kakaoLoginService;
    @Autowired
    public RedisService redisService;
    @Autowired
    public UserService userService;


    @GetMapping("")
    public void KakaoLogin() {
        System.out.println("KakaoLoginController/KakaoLogin");
        // 버튼 눌렀을때 로그인창 뜨게하는거 백에서 구현할거면 여기 사용
    }

    @GetMapping("/callback")
    public ResponseEntity KakaoLoginCallBack(@RequestParam(value = "code") String code) throws IOException {
        System.out.println("KakaoLoginController/KakaoLoginCallBack");
        KakoLoginResponseDTO kakoLoginResponseDTO = null;

        // 엑세스/리프레쉬 토큰 받기
        HashMap<String, String> kakaoToken  = kakaoLoginService.getAccessToken(code);

        // 유저 정보 받기
        HashMap<String, Object> kakaoUserInfo = kakaoLoginService.getUserInfo(kakaoToken.get("accessToken"));
        String accessToken = kakaoToken.get("accessToken");
        String refreshToken = kakaoToken.get("refreshToken");
        String email = (String) kakaoUserInfo.get("email");

        // email로 회원 찾기
        Optional<Member> member = userService.findUser(email);


        if (member.isPresent()) {   //회원 -> 로그인처리

            //redis에서 refreshToken 칮기
            String prevRefreshToken = redisService.getRefreshToken(email);
            kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken, prevRefreshToken,member.get().getName());


        }else{  //비회원 ->가입
            kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken, refreshToken, "홍길동");

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setName("홍길동");  // 닉네임 랜덤으로 부여
            loginDTO.setEmail(email);
            loginDTO.setPassword("");
            loginDTO.setPlatform(Platform.KAKAO);
            loginDTO.setPlatformId("1");

            userService.save(loginDTO);

            //redis에 refreshToken 저장
            redisService.setValues(email, refreshToken);

        }

//        //accessToken 만료되었는지 검사
//        boolean isTokenValid = kakaoLoginService.isValidToken("KMXxzLPp_GjjTaMW1-3Z8t2GmCRxTqV9AAAAAQopyV8AAAGQplhQWxKZRqbpl2cW");
//        System.out.println("accessToken 유효한지: "+isTokenValid);

        return ResponseEntity.ok().body(kakoLoginResponseDTO);
    }
}
