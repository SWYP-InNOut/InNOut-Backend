package inandout.backend.repository.post;

import inandout.backend.entity.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageJPARepository extends JpaRepository<PostImage, Integer> {

    @Query("SELECT pi.postImgUrl FROM PostImage pi WHERE pi.post.id = :post_id")
    List<String> findUrlByPostId(@Param(value = "post_id") Integer postId);

    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :post_id")
    void deleteByPostId(@Param(value = "post_id") Integer postId);

}
