package inandout.backend.repository.post;

import inandout.backend.entity.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageJPARepository extends JpaRepository<PostImage, Integer> {

}
