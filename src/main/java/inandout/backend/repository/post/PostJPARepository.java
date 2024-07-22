package inandout.backend.repository.post;

import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJPARepository extends JpaRepository<Post, Integer> {

}
