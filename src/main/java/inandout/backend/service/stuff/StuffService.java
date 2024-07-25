package inandout.backend.service.stuff;

import inandout.backend.common.exception.MemberException;
import inandout.backend.dto.others.InOutRequestDTO;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.post.InOut;
import inandout.backend.entity.post.Post;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.repository.post.InOutRepository;
import inandout.backend.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_MEMBER;

@Service
public class StuffService {
    @Autowired
    public InOutRepository inOutRepository;
    @Autowired
    public PostRepository postRepository;
    @Autowired
    public MemberRepository memberRepository;

    public void saveIn(InOutRequestDTO inOutRequestDTO) throws Exception {
        System.out.println("saveIn");
        System.out.println(inOutRequestDTO.getIsMember());
        Post post = postRepository.getPostByPostId(inOutRequestDTO.getPostId());

        Optional<Member> member;
        if (inOutRequestDTO.getIsMember()) { // 회원이 누른거면
            System.out.println("회원임");
            try {
                member = memberRepository.findById(inOutRequestDTO.getMemberId());
            } catch (Exception e) {
                member = null;
                throw new MemberException(NOT_FOUND_MEMBER);
            }

        }else{
            member = null;
        }

        InOut inOut = new InOut(true, false, inOutRequestDTO.getIsMember(), post, member.get());

        inOutRepository.save(inOut);

    }

    public void saveOut(InOutRequestDTO inOutRequestDTO) throws Exception {
        System.out.println("saveOut");
        System.out.println(inOutRequestDTO.getIsMember());
        Post post = postRepository.getPostByPostId(inOutRequestDTO.getPostId());

        Optional<Member> member;
        if (inOutRequestDTO.getIsMember()) { // 회원이 누른거면
            System.out.println("회원임");
            try {
                member = memberRepository.findById(inOutRequestDTO.getMemberId());
            } catch (Exception e) {
                member = null;
                throw new MemberException(NOT_FOUND_MEMBER);
            }

        }else{
            member = null;
        }

        InOut inOut = new InOut(false, true, inOutRequestDTO.getIsMember(), post, member.get());

        inOutRepository.save(inOut);

    }

}
