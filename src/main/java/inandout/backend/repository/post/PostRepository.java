package inandout.backend.repository.post;

import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
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

        //System.out.println("psotUrl: "+postImgUrl);
        return null;

    }

}
