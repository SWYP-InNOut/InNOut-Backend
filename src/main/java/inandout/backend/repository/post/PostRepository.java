package inandout.backend.repository.post;

import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    public Post getPostByPostId(Integer postId){
        Post post = (Post) em.createQuery("SELECT p FROM Post p WHERE p.id = :post_id")
                .setParameter("post_id", postId).getSingleResult();
        return post;
    }


    public String getOldestPostImage(Integer postId) {
        System.out.println("postId: "+postId);
        List<String> postImageList = em.createQuery("SELECT pi.postImgUrl FROM PostImage pi WHERE pi.post.id = :post_id ORDER BY pi.createdAt ASC")
                .setParameter("post_id", postId).getResultList();

        for (String postImage : postImageList) {
            System.out.println(postImage);
            return postImage;
        }

        return null;

    }

    public String getStuffNameByPostId(Integer postId) {
        String stuffName = (String) em.createQuery("SELECT p.title FROM Post p WHERE p.id = : post_id")
                .setParameter("post_id", postId).getSingleResult();
        return stuffName;
    }

    public List<Post> getPostListByMemberId(Integer memberId) {
        List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.member.id = :member_id")
                .setParameter("member_id", memberId).getResultList();
        return posts;
    }

    public LocalDateTime getRecentPostDateByMemberId(Integer memberId) {
        LocalDateTime postCreatedAt = (LocalDateTime) em.createQuery("SELECT MAX(p.createdAt) FROM Post p WHERE p.member.id = :member_id")
                .setParameter("member_id", memberId).getSingleResult();
        System.out.println("최근 발행일: "+postCreatedAt);
        return postCreatedAt;
    }

    @Transactional
    public void updateInCount(Integer postId, Integer newInCount) {
        em.createQuery("UPDATE Post p SET p.inCount = :new_in_count WHERE p.id = :post_id")
                .setParameter("new_in_count", newInCount).setParameter("post_id", postId).executeUpdate();

    }

    @Transactional
    public void updateOutCount(Integer postId, Integer newOutCount) {
        em.createQuery("UPDATE Post p SET p.outCount = :new_out_count WHERE p.id = :post_id")
                .setParameter("new_out_count", newOutCount).setParameter("post_id", postId).executeUpdate();

    }
}
