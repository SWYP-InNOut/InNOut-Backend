package inandout.backend.repository.post;

import inandout.backend.entity.post.InOut;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class InOutRepository {
    private final EntityManager em;

    @Transactional
    public void save(InOut inOut) {
        em.persist(inOut);
    }

}
