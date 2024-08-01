package inandout.backend.controller.member;

import inandout.backend.argumentresolver.MemberId;
import inandout.backend.common.exception.BaseException;
import inandout.backend.common.response.BaseErrorResponse;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.member.IsPublicDTO;
import inandout.backend.dto.member.ModifyUserRequestDTO;
import inandout.backend.dto.member.NicknameDTO;
import inandout.backend.dto.member.PasswordDTO;
import inandout.backend.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.DUPLICATED_NICKNAME;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/nickname")
    public BaseResponse<String> setNickname(@MemberId Integer memberId, @RequestBody NicknameDTO nickname) {
        System.out.println("들어옴");
        System.out.println("email:::: "+memberId);
        memberService.updateNickname(memberId, nickname.getNickname());
        return new BaseResponse<>("닉네임 설정이 완료되었습니다.");
    }

    @PostMapping("/check-password")
    public BaseResponse<String> validatePassword(@MemberId Integer memberId, @RequestBody PasswordDTO password) {
        log.info(memberId.toString());
        memberService.validatePassword(memberId, password.getPassword());
        return new BaseResponse<>("비밀번호 확인이 완료되었습니다.");
    }

    @PostMapping("/password")
    public BaseResponse<String> updatePassword(@MemberId Integer memberId, @RequestBody PasswordDTO password) {
        log.info(memberId.toString());
        memberService.updatePassword(memberId, password.getPassword());
        return new BaseResponse<>("비밀번호 변경이 완료되었습니다.");
    }

    @PostMapping("/ispublic")
    public BaseResponse<IsPublicDTO> updateIsPublic(@MemberId Integer memberId) {
        log.info(memberId.toString());
        IsPublicDTO isPublicDTO = memberService.updateIsPublic(memberId);
        return new BaseResponse<>(isPublicDTO);
    }

    @PostMapping("/user/modify")
    public BaseResponse<String> modifyUser(@RequestBody ModifyUserRequestDTO modifyUserRequestDTO) {
        System.out.println("modifyUser");
        // 닉네임 저장 (중복확인)
        boolean isDuplicate = memberService.isDuplicateNickname(modifyUserRequestDTO.getNickname(), modifyUserRequestDTO.getMemberId());

        if (isDuplicate) {
            throw  new BaseException(DUPLICATED_NICKNAME);
           // return new BaseResponse<>("중복된 닉네임입니다.");
        }

        // 프로필 update
        memberService.updateProfile(modifyUserRequestDTO);

        return new BaseResponse<>("프로필 변경에 성공하였습니다.");

    }

}
