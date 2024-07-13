package inandout.backend.healthcheck;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Getter
@Transactional
public class HealthCheckRepository {
    private final EntityManager em;

    public Long save(HealthDomain oauthMember) {
        log.info(oauthMember.getName());
        em.persist(oauthMember);
        return oauthMember.getId();
    }

}
