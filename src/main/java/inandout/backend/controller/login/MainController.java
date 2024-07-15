package inandout.backend.controller.login;

import inandout.backend.dto.login.CustomMemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {
    @GetMapping("/main")
    public String mainP() {
        log.info(((CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        String email = ((CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return "Main Controller" + email;
    }
}
