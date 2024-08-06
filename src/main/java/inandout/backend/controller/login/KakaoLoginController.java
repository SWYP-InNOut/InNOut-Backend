package inandout.backend.controller.login;


import com.fasterxml.jackson.databind.ObjectMapper;
import inandout.backend.common.exception.BaseException;
import inandout.backend.common.exception.MemberException;
import inandout.backend.common.response.BaseErrorResponse;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.KakaoNicknameRequestDTO;
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
import inandout.backend.service.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_NICKNAME;


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
    @Autowired
    public MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/local")
    public void KakaoLoginLocal(@RequestParam(value = "code") String code) {
        System.out.println("KakaoLoginController/KakaoLoginLocal -> TEST");
        System.out.println("Test로 들어온 code: "+code);

    }

    @GetMapping("/callback")
    public void KakaoLogin(@RequestParam(value = "code") String code) {
        System.out.println("KakaoLoginController/KakaoLogin -> TEST");
        System.out.println("Test로 들어온 code: "+code);

    }


    @GetMapping("")
    public ResponseEntity<KakoLoginResponseDTO> KakaoLoginCallBack(@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        System.out.println("KakaoLoginController/KakaoLoginCallBack");
        KakoLoginResponseDTO kakoLoginResponseDTO = null;

        // 엑세스/리프레쉬 토큰 받기
        HashMap<String, String> kakaoToken  = kakaoLoginService.getAccessToken(code);

        // 유저 정보 받기
        HashMap<String, Object> kakaoUserInfo = kakaoLoginService.getUserInfo(kakaoToken.get("accessToken"));


        String email = (String) kakaoUserInfo.get("email");
        boolean isMember;

        // email로 회원 찾기
        Optional<Member> member = userService.findKakaoUser(email);

        Integer memberImageId;

        if (member.isPresent()) {   //회원 -> 로그인처리
            System.out.println("회원임");
            memberImageId = member.get().getMemberImageId();
            isMember = true;

        }else{  //비회원 ->가입
            System.out.println("비회원임");
            // 프로필 이미지 랜덤 생성
            memberImageId = (int) ((Math.random()*6)+1);
            // 닉네임 랜덤으로 부여
            LoginDTO loginDTO = new LoginDTO("홍길동", email, "", Platform.KAKAO, "1", MemberStatus.INACTIVE,memberImageId);


            System.out.println("카카오 회원가입!");
            userService.save(loginDTO);
            isMember = false;


        }
        System.out.println("프로필 이미지: "+memberImageId);
        Member loginMember = memberRepository.findKakaoMemberByEmail(email).get();
        System.out.println("loginMember; "+loginMember.getId());

        TokenInfo tokenInfo= jwtUtil.generateToken(loginMember.getId());
        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        //redis에 refreshToken 저장
        redisService.setValues(refreshToken, loginMember.getId());

        kakoLoginResponseDTO = new KakoLoginResponseDTO(accessToken, isMember, loginMember.getId(), memberImageId);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.addHeader("Authorization", tokenInfo.getGrantType() + " " + accessToken);
        response.setHeader(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; Path=/; HttpOnly; Secure; Max-Age=" + refreshTokenValidTime + "; SameSite=None");

        return ResponseEntity.ok(kakoLoginResponseDTO);

//        //accessToken 만료되었는지 검사
//        boolean isTokenValid = kakaoLoginService.isValidToken("KMXxzLPp_GjjTaMW1-3Z8t2GmCRxTqV9AAAAAQopyV8AAAGQplhQWxKZRqbpl2cW");
//        System.out.println("accessToken 유효한지: "+isTokenValid);

    }

    @PostMapping("/nickname")
    public BaseResponse KakaoLoginNickname(@RequestBody KakaoNicknameRequestDTO kakaoNicknameRequestDTO) {
        System.out.println("KakaoLoginController/KakaoLoginNickname");
        Integer memberId = kakaoNicknameRequestDTO.getMemberId();
        String nickname = kakaoNicknameRequestDTO.getNickname();

        //닉네임 중복 확인
        if (memberService.isDuplicateNickname(nickname, memberId)) {
            // 중복
            BaseException baseException = new BaseException(DUPLICATED_NICKNAME);
            throw new MemberException(DUPLICATED_NICKNAME);
        }

        // 닉네임 변환 & ACTIVE로 변환
        memberService.updateNickname(memberId, nickname);

        return new BaseResponse("success");
    }
}
