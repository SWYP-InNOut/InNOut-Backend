package inandout.backend.service.post;

import inandout.backend.dto.chat.ChatResponseDTO;
import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.entity.post.Post;
import inandout.backend.repository.chat.ChatRepository;
import inandout.backend.repository.post.InOutRepository;
import inandout.backend.repository.post.PostImageJPARepository;
import inandout.backend.repository.post.PostJPARepository;
import inandout.backend.repository.post.PostRepository;
import inandout.backend.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostJPARepository postJPARepository;

    @Autowired
    public ChatRepository chatRepository;

    @Autowired
    public ChatService chatService;

    @Autowired
    public PostImageJPARepository postImageJPARepository;

    @Autowired
    public InOutRepository inOutRepository;

    public PostResponseDTO getPost(Integer memberId, Integer postId) {
        //postId로 memberId -> memberName
        Integer ownerId = chatRepository.getMemberIdByPostId(postId);
        String ownerName = chatRepository.getMemberNameByMemberId(ownerId);

        //postId로 post객체
        Optional<Post> post = postJPARepository.findById(postId);
        String title = post.get().getTitle();
        String inContent = post.get().getInContent();
        String outContent = post.get().getOutContent();

        //in/out 선택했는지 여부 가져오기
        List<?> isCheckedInfo = inOutRepository.getIsCheckedInfo(memberId, postId);
        boolean isCheckedIn = false;
        boolean isCheckedOut = false;

        for (Object o : isCheckedInfo) {
            Object[] result = (Object[]) o;
            boolean resultIn = (boolean) result[0];
            boolean resultOut = (boolean) result[1];

            if (resultIn) {
                isCheckedIn = resultIn;
            }
            if (resultOut) {
                isCheckedOut = resultOut;
            }
        }

        // 상위 5개 가져오기(임의)
        List<ChatResponseDTO> chatResponseDTOS = chatService.getPostChat(memberId, postId);
        List<ChatResponseDTO> chatResponseDTOList;
        if(chatResponseDTOS.size() > 5){
            chatResponseDTOList = chatResponseDTOS.subList(0, 5);
        }else{
            chatResponseDTOList = chatResponseDTOS;
        }

        // 이미지 URLs 가져오기
        List<String> imageUrls = postImageJPARepository.findUrlByPostId(postId);

        PostResponseDTO postResponseDTO = new PostResponseDTO(ownerName, title, inContent, outContent,  LocalDateTime.now(ZoneId.of("Asia/Seoul")), isCheckedIn, isCheckedOut, chatResponseDTOList, imageUrls);

        return postResponseDTO;
    }

    public Integer updateInCount(Integer postId, Integer inType) {
        // post찾고
        Post post = postRepository.getPostByPostId(postId);
        //update 쿼리
        Integer newInCount = post.getInCount() + inType;

        postRepository.updateInCount(postId, newInCount);

        return newInCount;
    }

    public Integer plusOutCount(Integer postId) {
        // post찾고
        Post post = postRepository.getPostByPostId(postId);
        //update 쿼리
        Integer newOutCount = post.getOutCount() + 1;

        postRepository.updateOutCount(postId, newOutCount);

        return newOutCount;
    }



}
