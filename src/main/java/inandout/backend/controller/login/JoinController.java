package inandout.backend.controller.login;

import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.service.login.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/join")
    public BaseResponse<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return new BaseResponse<>("인증메일을 보냈습니다.");
    }

    @GetMapping("/auth/verify")
    public Object verifyEmail(@RequestParam("token") String token) {
        boolean isComplete = joinService.updateByVerifyToken(token);

        if (isComplete) {
            return new RedirectView("http://stuffinout.site/login");
        } else {
            return new RedirectView("http://stuffinout.site/error");    // 링크 만료 페이지로 이동
        }
    }
}
