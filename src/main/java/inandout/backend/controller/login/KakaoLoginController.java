package inandout.backend.controller.login;


import inandout.backend.dto.login.KakoLoginResponseDTO;
import inandout.backend.service.login.KakaoLoginService;
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

    @GetMapping("")
    public void KakaoLogin() {
        System.out.println("KakaoLoginController/KakaoLogin");
        // 버튼 눌렀을때 로그인창 뜨게하는거 백에서 구현할거면 여기 사용
    }

    @GetMapping("/callback")
    public ResponseEntity KakaoLoginCallBack(@RequestParam(value = "code") String code) throws IOException {
        System.out.println("KakaoLoginController/KakaoLoginCallBack");

        // 엑세스 토큰 받기
        String accessToken = kakaoLoginService.getAccessToken(code);

        HashMap<String, Object> kakaoUserInfo = kakaoLoginService.getUserInfo(accessToken);
        String email = (String) kakaoUserInfo.get("email");

        KakoLoginResponseDTO kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken, email);

        return ResponseEntity.ok().body(kakoLoginResponseDTO);
    }
}
