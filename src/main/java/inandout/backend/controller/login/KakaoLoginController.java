package inandout.backend.controller.login;


import inandout.backend.dto.login.KakoLoginResponseDTO;
import inandout.backend.service.login.KakaoLoginService;
import inandout.backend.service.login.LoginService;
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


@RestController
@RequestMapping("/kakaologin")
public class KakaoLoginController {

    @Autowired
    public KakaoLoginService kakaoLoginService;
    @Autowired
    public RedisService redisService;
    @Autowired
    public LoginService loginService;
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

        //존재하는 회원인지 확인
        boolean isUser = loginService.isUser(email);

        //회원이면 로그인처리
        if (isUser) {
            //유저 닉네임, email, id, accessToken 등 전달

            // 1. 유저정보(email)로

        }else{  //비회원이면 가입하고 유저 정보 반환(id, email, nickname, accessToken, refreshToken)
            kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken, refreshToken, email);


                /*

                저장

                    */
            //redis에 refreshToken 저장
            redisService.setValues(email, refreshToken);

        }





        //accessToken 만료되었는지 검사
        boolean isTokenValid = kakaoLoginService.isValidToken("KMXxzLPp_GjjTaMW1-3Z8t2GmCRxTqV9AAAAAQopyV8AAAGQplhQWxKZRqbpl2cW");
        System.out.println("accessToken 유효한지: "+isTokenValid);

        return ResponseEntity.ok().body(kakoLoginResponseDTO);
    }
}
