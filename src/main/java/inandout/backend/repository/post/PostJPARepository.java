package inandout.backend.repository.post;


import inandout.backend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJPARepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer postId);


}
