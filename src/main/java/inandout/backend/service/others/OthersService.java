package inandout.backend.service.others;

import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.dto.others.OthersResponseDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.Post;
import inandout.backend.entity.post.PostImage;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.post.PostImageJPARepository;
import inandout.backend.repository.post.PostJPARepository;
import inandout.backend.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OthersService {

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostImageJPARepository postImageJPARepository;


    public List<OthersResponseDTO> getOthersList() {
        List<OthersResponseDTO> othersResponseDTOList = new ArrayList<>();

        // isPublic true인 사용자 id
        List<Integer> members = memberRepository.getMemberIsPublic();

        //그 id 중에서만 정렬
        List<Post> posts = postRepository.getPostByUserCount(members);

        //DTO 생성
        for (Post post : posts) {
            othersResponseDTOList.add(getOthersDTO(post));
        }

        return othersResponseDTOList;
    }

    public OthersResponseDTO getOthersDTO(Post post){


        String memberName = post.getMember().getName();
        Integer memberId = post.getMember().getId();
        List<String> imageUrls = postImageJPARepository.findUrlByPostIdOrderByCreatedAt(post.getId());
        String imageUrl = null;
        if (imageUrls.size() != 0) {
            imageUrl = imageUrls.get(0);
        }
        Integer memberImageId = memberRepository.getMemberImageId(memberId);

        OthersResponseDTO othersResponseDTO = new OthersResponseDTO(memberName, memberId, imageUrl, memberImageId);

        return othersResponseDTO;

    }


}
