package inandout.backend.repository.post;

import inandout.backend.entity.post.PostImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageJPARepository extends JpaRepository<PostImage, Integer> {

    @Query("SELECT pi.postImgUrl FROM PostImage pi WHERE pi.post.id = :post_id")
    List<String> findUrlByPostId(@Param(value = "post_id") Integer postId);

    @Query("SELECT pi.postImgUrl FROM PostImage pi WHERE pi.post.id = :post_id ORDER BY pi.createdAt ASC")
    List<String> findUrlByPostIdOrderByCreatedAt(@Param(value = "post_id") Integer postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :post_id")
    void deleteByPostId(@Param(value = "post_id") Integer postId);

//

}
