package inandout.backend.controller.post;

import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    @Autowired
    public PostService postService;

    @PostMapping("/in")
    public ResponseEntity inController(@RequestParam(value = "postId") Integer postId) {
        log.info("in");
        postService.plusInCount(postId);

        return ResponseEntity.ok("in증가");
    }

    @PostMapping("/out")
    public ResponseEntity outController(HttpServletRequest request, @RequestParam(value = "postId") Integer postId) {
        log.info("out");
        HttpSession session = request.getSession();
        System.out.println("Session: "+session.getId());
        postService.plusOutCount(postId);

        return ResponseEntity.ok("out 증가");
    }

}
