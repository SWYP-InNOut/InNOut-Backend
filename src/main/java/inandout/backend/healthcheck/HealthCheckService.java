package inandout.backend.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    private final HealthCheckRepository healthRepository;

    public Long saveUser(HealthDomain oauthMember){
        Long savedUser = healthRepository.save(oauthMember);
        return savedUser;
    }
}
