package inandout.backend.controller.login;

import inandout.backend.common.response.BaseResponse;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.service.login.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/join")
    public BaseResponse<String> joinProcess(@ModelAttribute JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return new BaseResponse<>("회원가입에 성공하였습니다.");
    }
}
