package inandout.backend.healthcheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    @GetMapping("/api/test/connection")
    public ResponseEntity<Integer> testConnection2() {
        log.info("testConnection2");
        return ResponseEntity.ok(2);
    }

    @GetMapping("/healthcheck")
    public String testConnection() {

        log.info("testConnection");
        return "SUCCESS";
    }

    @PostMapping("/healthcheckDB")
    public Long testDBConnection(@RequestBody HealthDomain healthDomain) {
        log.info(healthDomain.getName());
        return healthCheckService.saveUser(healthDomain);

    }
}
