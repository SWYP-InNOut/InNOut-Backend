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


    public List getIsCheckedInfo(Integer memberId, Integer postId) {

        // 투표했는지 여부 체크
        List resultList= em.createQuery("SELECT io.isCheckIn, io.isCheckOut FROM InOut io WHERE member.id = :member_id AND post.id = :post_id")
                .setParameter("member_id", memberId).setParameter("post_id", postId).getResultList();

        return resultList;

    }
}
