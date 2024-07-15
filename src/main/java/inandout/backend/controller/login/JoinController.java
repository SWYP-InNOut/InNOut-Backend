package inandout.backend.controller.login;

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
    public String joinProcess(@ModelAttribute JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return "ok";
    }
}