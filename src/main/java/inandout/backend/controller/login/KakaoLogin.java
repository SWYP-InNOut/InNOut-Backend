package inandout.backend.controller.login;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;




@Slf4j
@RestController
public class KakaoLogin {
    @GetMapping("/kakaologin")
    public void KakaoLogin() {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
//        url.append("client_id="+clientId);
//        url.append("&redirect_uri="+redirectUri);
//        url.append("&response_type=code");
//        return "redirect:" + url.toString();
    }
}
