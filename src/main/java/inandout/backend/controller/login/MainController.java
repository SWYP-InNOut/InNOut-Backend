package inandout.backend.controller.login;

import inandout.backend.argumentresolver.MemberEmail;
import inandout.backend.dto.login.CustomMemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @GetMapping("/main")
    public String mainP(@MemberEmail String email) {
        return "Main Controller " + email;
    }
}
