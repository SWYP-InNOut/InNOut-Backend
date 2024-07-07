package inandout.backend.healthcheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HealthCheckController {
    @GetMapping("/healthcheck")
    public String testConnection() {
        return "Im not ok";
    }
}
