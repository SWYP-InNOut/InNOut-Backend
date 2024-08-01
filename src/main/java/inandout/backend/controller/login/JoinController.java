package inandout.backend.controller.login;

import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.dto.member.JoinResponseDTO;
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
    public BaseResponse<JoinResponseDTO> joinProcess(@RequestBody JoinDTO joinDTO) {
        Integer memberImageId = joinService.joinProcess(joinDTO);

        JoinResponseDTO joinResponseDTO = new JoinResponseDTO("인증메일을 보냈습니다.", memberImageId);

        return new BaseResponse<>(joinResponseDTO);
        //return new BaseResponse<>("인증메일을 보냈습니다."+memberImageId);

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
