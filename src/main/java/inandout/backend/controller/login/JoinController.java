package inandout.backend.controller.login;

import inandout.backend.common.exception.MemberException;
import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.service.login.JoinService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.FAILED_EMAIL_CERTIFICATION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/join")
    public BaseResponse<String> joinProcess(@ModelAttribute JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return new BaseResponse<>("인증메일을 보냈습니다.");
    }

    @GetMapping("/auth/verify")
    public Object verifyEmail(HttpSession session, @RequestParam("token") String token) {
        Optional<Member> member = joinService.updateByVerifyToken(token);

        if (member.isPresent()) {
            return new BaseResponse<>("회원가입에 성공하였습니다.");
        } else {
            log.error(FAILED_EMAIL_CERTIFICATION.getMessage());
            throw new MemberException(FAILED_EMAIL_CERTIFICATION);
        }
    }
}
