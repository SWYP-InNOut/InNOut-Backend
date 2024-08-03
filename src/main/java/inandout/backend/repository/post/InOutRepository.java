package inandout.backend.repository.post;

import inandout.backend.entity.post.InOut;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class InOutRepository {
    private final EntityManager em;

    @Transactional
    public void save(InOut inOut) {
        em.persist(inOut);
    }

    public InOut getIsCheckedInfo(int memberId, int postId) {
        // 투표했는지 여부 체크
        List<InOut> inout = em.createQuery("select io from InOut io where io.member.id = :member_id and io.post.id = :post_id", InOut.class)
                .setParameter("member_id", memberId)
                .setParameter("post_id", postId)
                .getResultList();
                //.getSingleResult();


        if (inout.size() != 0) {
            for(InOut io : inout){
                return io;
            }
        }

        return null;
    }

    public Boolean getExistMember(int memberId, int postId) {
        return em.createQuery("select count(io) > 0 from InOut io where io.member.id = :member_id and io.post.id = :post_id", Boolean.class)
                .setParameter("member_id", memberId)
                .setParameter("post_id", postId)
                .getSingleResult();
    }
}
