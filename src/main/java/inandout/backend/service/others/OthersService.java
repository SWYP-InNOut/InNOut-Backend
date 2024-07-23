package inandout.backend.service.others;

import inandout.backend.dto.myroom.PostResponseDTO;
import inandout.backend.dto.others.OthersResponseDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.Post;
import inandout.backend.repository.login.MemberRepository;
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


    public List<OthersResponseDTO> getOthersList() {
        List<OthersResponseDTO> othersResponseDTOList = new ArrayList<>();

        // member에서 usercount 많은 순으로 가져옴
        List<Member> members = memberRepository.getMembersOrderByUserCount();

        for (Member member : members) {
            othersResponseDTOList.add(getOthersDTO(member));
        }

        return othersResponseDTOList;
    }

    public OthersResponseDTO getOthersDTO(Member member){

        String memberName = member.getName();
        Integer memberId = member.getId();

        //게시물 List
        List<Post> postList = postRepository.getPostListByMemberId(memberId);
        Integer postCount = postList.size();

        Integer inCount = 0;
        Integer outCount = 0;
        for (Post post : postList) {
            inCount += post.getInCount();
            outCount += post.getOutCount();
        }

        Integer userCount = member.getUserCount();
        LocalDateTime recentPostDate = postRepository.getRecentPostDateByMemberId(memberId);

        OthersResponseDTO othersResponseDTO = new OthersResponseDTO(memberName, memberId, postCount, inCount, outCount, userCount, recentPostDate);
        return othersResponseDTO;

    }


}
