package inandout.backend.controller.member;

import inandout.backend.argumentresolver.MemberEmail;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.member.NicknameDTO;
import inandout.backend.dto.member.PasswordDTO;
import inandout.backend.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/nickname")
    public BaseResponse<String> setNickname(@MemberEmail String email, @RequestBody NicknameDTO nickname) {
        System.out.println("들어옴");
        System.out.println("email:::: "+email);
        memberService.updateNickname(email, nickname.getNickname());
        return new BaseResponse<>("닉네임 설정이 완료되었습니다.");
    }

    @GetMapping("/password")
    public BaseResponse<String> validatePassword(@MemberEmail String email, @RequestBody PasswordDTO password) {
        log.info(email);
        memberService.validatePassword(email, password.getPassword());
        return new BaseResponse<>("비밀번호 확인이 완료되었습니다.");
    }

    @PostMapping("/password")
    public BaseResponse<String> updatePassword(@MemberEmail String email, @RequestBody PasswordDTO password) {
        log.info(email);
        memberService.updatePassword(email, password.getPassword());
        return new BaseResponse<>("비밀번호 변경이 완료되었습니다.");
    }
}
