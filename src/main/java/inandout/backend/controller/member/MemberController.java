package inandout.backend.controller.member;

import inandout.backend.argumentresolver.MemberEmail;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.member.NicknameDTO;
import inandout.backend.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        memberService.updateNickname(email, nickname.getNickname());
        return new BaseResponse<>("닉네임 설정이 완료되었습니다.");
    }
}
