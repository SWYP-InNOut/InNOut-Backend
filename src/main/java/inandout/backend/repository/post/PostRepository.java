package inandout.backend.repository.post;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public List<Integer> getPostIdsByMemberId(Integer memberId) {
        List<Integer> postIdList = em.createQuery("SELECT p.id FROM Post p WHERE p.member.id = :member_id")
                .setParameter("member_id", memberId).getResultList();

        return postIdList;
    }
}
